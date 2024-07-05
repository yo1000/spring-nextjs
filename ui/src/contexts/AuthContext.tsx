'use client';

import React, {createContext, ReactNode, useContext, useEffect, useMemo, useState} from 'react';
import {User, UserManager} from 'oidc-client-ts';

type AuthContextType = {
    user: User | null;
    signinRedirect: () => void;
    signoutRedirect: () => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({children, oidcConfig}: { children: ReactNode, oidcConfig: any }) => {
    const [user, setUser] = useState<User | null>(null);
    const [eventAdded, setEventAdded] = useState(false);

    const userManager = useMemo(() => new UserManager(oidcConfig), [oidcConfig]);

    useEffect(() => {
        const handleUserLoaded = (user: User) => setUser(user);
        const handleUserUnloaded = () => setUser(null);

        if (!eventAdded) {
            userManager.events.addUserLoaded(handleUserLoaded);
            userManager.events.addUserUnloaded(handleUserUnloaded);
            userManager.events.addSilentRenewError(handleUserUnloaded);
            setEventAdded(true);
        }

        userManager.getUser().then((u) => {
            if (!u) {
                setUser(null);
                // // Sign-in redirect if user has not yet loaded
                // userManager.signinRedirect();
                return;
            }

            if (u.access_token !== user?.access_token) {
                setUser(u);
            }
        });

        if (!user?.access_token) {
            userManager.signinRedirectCallback().then((user) => {
                setUser(user);
            }).catch((error) => {
                console.error('signinRedirectCallback error', error);
            });
        }
    }, [userManager]);

    const signinRedirect = () => {
        void userManager.signinRedirect();
    };

    const signoutRedirect = () => {
        void userManager.signoutRedirect();
    };

    return (
        <AuthContext.Provider value={{user, signinRedirect, signoutRedirect}}>
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
