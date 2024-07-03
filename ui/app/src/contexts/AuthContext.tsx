'use client';

import React, {createContext, ReactNode, useContext, useEffect, useMemo, useState} from 'react';
import {User, UserManager} from 'oidc-client-ts';

type AuthContextType = {
    user: User | null;
    signinRedirect: () => void;
    signoutRedirect: () => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children, oidcConfig }: { children: ReactNode, oidcConfig: any }) => {
    const [user, setUser] = useState<User | null>(null);

    const userManager = useMemo(() => new UserManager(oidcConfig), [oidcConfig]);

    useEffect(() => {
        const handleUserLoaded = (user: User) => setUser(user);
        const handleUserUnloaded = () => setUser(null);

        userManager.getUser().then((user) => {
            if (user) {
                setUser(user);
            // } else {
            //     // Sign-in redirect if user has not yet loaded
            //     userManager.signinRedirect();
            }
        });

        userManager.events.addUserLoaded(handleUserLoaded);
        userManager.events.addUserUnloaded(handleUserUnloaded);

        userManager.signinRedirectCallback().then((user) => {
            setUser(user);
        }).catch((error) => {
            console.error('signinRedirectCallback error', error);
        });

        // Cleanup function to remove event listeners
        return () => {
            userManager.events.removeUserLoaded(handleUserLoaded);
            userManager.events.removeUserUnloaded(handleUserUnloaded);
        };
    }, [userManager]);

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
