import {AppProps} from 'next/app';
import {AuthProvider} from '@/context/AuthContext';
import {useEffect, useState} from "react";
import {UserManagerSettings, WebStorageStateStore} from "oidc-client-ts";
import "../styles/globals.css";
import Layout from "@/components/Layout";

function App({Component, pageProps}: AppProps) {
    const [oidcConfig, setOidcConfig] = useState<UserManagerSettings | null>(null);

    useEffect(() => {
        const fetchOidcConfig = async () => {
            try {
                const resp = await fetch(`/config`);
                if (!resp?.ok) throw new Error(`Response error: ${resp?.statusText}`)

                const json = await resp.json();
                if (!json?.oidc) throw new Error(`Json error: ${json}`)

                setOidcConfig({
                    authority: json.oidc.authority,
                    client_id: json.oidc.clientId,
                    redirect_uri: json.oidc.redirectUri,
                    post_logout_redirect_uri: json.oidc.postLogoutRedirectUri,
                    response_type: "code",
                    scope: "openid profile email",
                    userStore: new WebStorageStateStore({store: window.localStorage}),
                    // loadUserInfo: true,
                    accessTokenExpiringNotificationTimeInSeconds: 30,
                });
            } catch (e) {
                console.info(e);

                setOidcConfig({
                    authority: process.env.NEXT_PUBLIC_OIDC_AUTHORITY as string,
                    client_id: process.env.NEXT_PUBLIC_OIDC_CLIENT_ID as string,
                    redirect_uri: process.env.NEXT_PUBLIC_OIDC_REDIRECT_URI as string,
                    post_logout_redirect_uri: process.env.NEXT_PUBLIC_OIDC_POST_LOGOUT_REDIRECT_URI as string,
                    response_type: "code",
                    scope: "openid profile email",
                    userStore: new WebStorageStateStore({store: window.localStorage}),
                    // loadUserInfo: true,
                    accessTokenExpiringNotificationTimeInSeconds: 30,
                });
            }
        };

        void fetchOidcConfig();
    }, []);

    if (!oidcConfig) {
        return <div>Loading...</div>;
    }

    return (
        <AuthProvider oidcConfig={oidcConfig}>
            <Layout>
                <Component {...pageProps}/>
            </Layout>
        </AuthProvider>
    );
}

export default App;
