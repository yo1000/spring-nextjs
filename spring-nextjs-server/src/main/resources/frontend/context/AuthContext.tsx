import React, {createContext, ReactNode, useContext, useEffect, useState} from 'react';
import {User, UserManager} from 'oidc-client-ts';

type AuthContextType = {
    user: User | null;
    signinRedirect: () => void;
    signoutRedirect: () => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children, oidcConfig }: { children: ReactNode, oidcConfig: any }) => {
    const [user, setUser] = useState<User | null>(null);

    const userManager = new UserManager(oidcConfig);

    useEffect(() => {
        userManager.getUser().then((user) => {
            if (user) {
                setUser(user);
            // } else {
            //     // ユーザが存在しない場合、サインインリダイレクトを行う
            //     userManager.signinRedirect();
            }
        });

        userManager.events.addUserLoaded((user) => {
            setUser(user);
        });

        userManager.events.addUserUnloaded(() => {
            setUser(null);
        });

        // 認証情報の検証
        userManager.signinRedirectCallback().then((user) => {
            setUser(user);
        }).catch((error) => {
            console.error('signinRedirectCallback error', error);
        });
    }, []);

    const signinRedirect = () => {
        userManager.signinRedirect();
    };

    const signoutRedirect = () => {
        userManager.signoutRedirect();
    };

    return (
        <AuthContext.Provider value={{ user, signinRedirect, signoutRedirect }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);

    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }

    return context;
};
