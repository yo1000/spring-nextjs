import React, { ReactNode } from 'react';
import {useAuth} from "@/context/AuthContext";
import Link from "next/link";

type LayoutProps = {
    children: ReactNode;
};

const Layout = ({ children }: LayoutProps) => {
    const {user, signinRedirect, signoutRedirect} = useAuth();

    return (
        <div>
            <header>
                <nav>
                    {user ? (
                        <ul>
                            <li><Link href={`/`}>Home</Link></li>
                            <li><Link href={`/demo`}>Demo</Link></li>
                            <li>{user?.profile?.preferred_username}</li>
                            <li>
                                <button onClick={signoutRedirect}>Logout</button>
                            </li>
                        </ul>
                    ) : (
                        <ul>
                            <li><Link href={`/`}>Home</Link></li>
                            <li><Link href={`/demo`}>Demo</Link></li>
                            <li>
                                <button onClick={signinRedirect}>Login</button>
                            </li>
                        </ul>
                    )}
                </nav>
            </header>
            <main>{children}</main>
            <footer>
            <p>© 2024 yo1000</p>
            </footer>
        </div>
    );
};

export default Layout;
