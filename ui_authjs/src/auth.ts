import NextAuth, {Account} from "next-auth"
import Keycloak from "next-auth/providers/keycloak"
import {PrismaAdapter} from "@auth/prisma-adapter";
import {AdapterSession} from "@auth/core/adapters";
import {PrismaClient} from "@prisma/client";

//// Default basic implementations.
//// See: https://authjs.dev/getting-started/authentication/oauth
// export const { handlers, signIn, signOut, auth } = NextAuth({
//   providers: [Keycloak],
// });

// See:
// - https://authjs.dev/getting-started/adapters/prisma#prismaadapter
// - https://github.com/nextauthjs/next-auth/blob/next-auth%405.0.0-beta.29/docs/pages/getting-started/adapters/prisma.mdx
const ISSUER_URL = (process.env.KEYCLOAK_ISSUER || "").replace(/\/$/, "");
const LOGOUT_URL = `${ISSUER_URL}/protocol/openid-connect/logout`;
const TOKEN_URL = `${ISSUER_URL}/protocol/openid-connect/token`;

// TODO: Adopt Repository interface.
const prisma = new PrismaClient();

const backChannelLogout = async ({ refreshToken, }: {
  refreshToken: string | undefined | null;
}) => {
  if (refreshToken) {
    await fetch(LOGOUT_URL, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: new URLSearchParams({
        client_id: process.env.KEYCLOAK_CLIENT_ID!,
        client_secret: process.env.KEYCLOAK_CLIENT_SECRET!,
        refresh_token: refreshToken,
      }),
    }).catch(() => {});
  }
}

const clearSession = async ({ userId, sessionToken, }: {
  userId?: string;
  sessionToken?: string;
}) => {
  if (userId) {
    const account = await prisma.account.findFirst({
      where: { userId, provider: "keycloak" },
      select: {
        id: true,
        refresh_token: true,
      },
    })

    await prisma.account.update({
      where: {
        id: account?.id,
      },
      data: {
        access_token: null,
        refresh_token: null,
        id_token: null,
        expires_at: null,
      },
    })
  }

  if (sessionToken) {
    await prisma.session.deleteMany({
      where: {
        sessionToken: sessionToken,
      },
    });
  }
}

export const { handlers, signIn, signOut, auth, } = NextAuth({
  providers: [Keycloak({
    issuer: process.env.KEYCLOAK_ISSUER,
    clientId: process.env.KEYCLOAK_CLIENT_ID!,
    clientSecret: process.env.KEYCLOAK_CLIENT_SECRET!,
    checks: ["pkce", "state"],
    authorization: {
      params: {
        scope: "openid profile email",
      },
    },
  })],
  adapter: PrismaAdapter(prisma),
  events: {
    async signIn(message) {
      const account: Account = (message as any)?.account;

      await prisma.account.updateMany({
        where: {
          providerAccountId: account.providerAccountId,
          provider: account.provider,
        },
        data: {
          access_token: account.access_token ?? null,
          refresh_token: account.refresh_token ?? null,
          id_token: account.id_token ?? null,
          token_type: account.token_type ?? null,
          scope: account.scope ?? null,
          expires_at: account.expires_at ?? null,
        },
      });
    },
    async signOut(message) {
      const session: AdapterSession = (message as any)?.session;

      const userId = session?.userId;
      const sessionToken = session?.sessionToken;

      const account = await prisma.account.findFirst({
        where: {
          userId,
          provider: "keycloak",
        },
        select: {
          refresh_token: true,
        },
      });

      await backChannelLogout({ refreshToken: account?.refresh_token, });
      await clearSession({ userId, sessionToken, });
    },
  },
  callbacks: {
    async session({ session, user, }) {
      const account = await prisma.account.findFirst({
        where: {
          userId: user.id,
          provider: "keycloak",
        },
        select: {
          id: true,
          userId: true,
          provider: true,
          providerAccountId: true,
          access_token: true,
          id_token: true,
          refresh_token: true,
          expires_at: true,
        },
      });

      if (!account) return session;

      const now = Math.floor(Date.now() / 1000);
      const requiresRefresh = !account.expires_at || account.expires_at <= now + 60;

      // Not requires token refresh, then nop.
      if (!requiresRefresh) {
        return session;
      }

      // Illegal state
      if (!account.refresh_token) {
        await clearSession({
          userId: user.id,
          sessionToken: session.sessionToken,
        });

        return session;
      }

      let ensured = account;

      // Refresh access-token and refresh-token
      const updated = await (async (account: {
        id: string,
        refresh_token: string | null,
      }) => {
        if (!account.refresh_token) return undefined;

        const resp = await fetch(TOKEN_URL, {
          method: "POST",
          headers: { "Content-Type": "application/x-www-form-urlencoded" },
          body: new URLSearchParams({
            grant_type: "refresh_token",
            refresh_token: account.refresh_token,
            client_id: process.env.KEYCLOAK_CLIENT_ID!,
            client_secret: process.env.KEYCLOAK_CLIENT_SECRET!,
          })
        });

        if (resp.ok) {
          const json = await resp.json();

          const nextAccessTokenExpiresAt = typeof json.expires_in === "number"
            ? json.expires_in
            : null;

          return prisma.account.update({
            where: {
              id: account.id,
            },
            data: {
              access_token: json.access_token ?? null,
              refresh_token: json.refresh_token ?? account.refresh_token,
              id_token: json.id_token ?? null,
              token_type: json.token_type ?? null,
              scope: json.scope ?? null,
              expires_at: nextAccessTokenExpiresAt,
            },
            select: {
              access_token: true,
              id_token: true,
              refresh_token: true,
              expires_at: true,
            },
          });
        } else {
          await backChannelLogout({ refreshToken: account.refresh_token, });
          await clearSession({
            userId: user.id,
            sessionToken: session.sessionToken,
          });
        }
      })(account);

      if (updated) ensured = { ...ensured, ...updated, };

      (session as any).accessToken = ensured.access_token ?? null;
      (session as any).idToken = ensured.id_token ?? null;

      return session;
    }
  }
});
