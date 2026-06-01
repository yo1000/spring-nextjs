FROM node:26 as builder

ARG SRC

COPY $SRC /opt/app
WORKDIR /opt/app

RUN npm install


FROM node:26-slim as runner

COPY --from=builder /opt/app /opt/app

WORKDIR /opt/app

ENV DATABASE_URL="postgresql://postgres:postgres@localhost:5432/postgres?schema=authjs"
ENV KEYCLOAK_ISSUER="http://localhost:8000/realms/master"
ENV KEYCLOAK_CLIENT_ID="axum-nextjs"
ENV KEYCLOAK_CLIENT_SECRET="Secret-1234567890-Secret"
ENV NEXTAUTH_URL="http://localhost:3000"
ENV NEXTAUTH_SECRET="replace-with-a-strong-random-string"
ENV AUTH_TRUST_HOST=true
ENV NEXT_PUBLIC_API_BASE_URI="http://localhost:8080"

CMD ["sh", "-c", "npm exec prisma migrate dev -- --name init; npm run build && npm run start"]
