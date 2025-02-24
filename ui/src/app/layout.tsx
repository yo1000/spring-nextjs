'use client';

import {AuthProvider} from '@/contexts/AuthContext';
import React, {useEffect, useState} from "react";
import {UserManagerSettings, WebStorageStateStore} from "oidc-client-ts";
import {Noto_Color_Emoji, Noto_Sans, Noto_Sans_JP} from "next/font/google";
import "./globals.css";
import Navigator from "@/components/Navigator";

const noTofuEmoji = Noto_Color_Emoji({
    weight: "400",
    subsets: ["emoji"],
    display: "swap",
});
const noTofuJp = Noto_Sans_JP({
    subsets: ["latin"],
});
const noTofu = Noto_Sans({
    subsets: ["latin"],
});

export default function RootLayout({
   children,
}: Readonly<{
    children: React.ReactNode;
}>) {
    const [oidcConfig, setOidcConfig] = useState<UserManagerSettings | null>(null);

    useEffect(() => {
        const setupOidcConfig = async () => {
            setOidcConfig({
                authority: process.env.NEXT_PUBLIC_OIDC_AUTHORITY as string,
                client_id: process.env.NEXT_PUBLIC_OIDC_CLIENT_ID as string,
                redirect_uri: process.env.NEXT_PUBLIC_OIDC_REDIRECT_URI as string,
                post_logout_redirect_uri: process.env.NEXT_PUBLIC_OIDC_POST_LOGOUT_REDIRECT_URI as string,
                response_type: "code",
                scope: "openid profile email",
                userStore: new WebStorageStateStore({store: window.localStorage}),
                // loadUserInfo: true,
                // How many seconds before expiration should the access token be refreshed?
                accessTokenExpiringNotificationTimeInSeconds: 10,
            });
        };

        void setupOidcConfig();
    }, []);

    return (
        <html lang="en" className={`h-full bg-neutral-100`}>
        <body className={`h-full ${noTofuEmoji.className} ${noTofuJp.className} ${noTofu.className}`}>
        {oidcConfig
            ? <AuthProvider oidcConfig={oidcConfig}>
                <div className="min-h-full">
                    <Navigator/>
                    {children}
                </div>
            </AuthProvider>
            : <div>Loading...</div>
        }
        </body>
        </html>
    );
}
