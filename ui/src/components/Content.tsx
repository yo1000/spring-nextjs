'use client';

import {ReactNode} from "react";

type ContentProps = {
    title: string;
    children: ReactNode;
};

export default function Content({title, children}: ContentProps) {
    return (
        <>
            <header className="bg-white shadow-sm">
                <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
                    <h1 className="text-3xl font-bold tracking-tight text-neutral-900">
                        {title}
                    </h1>
                </div>
            </header>
            <main>
                <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
                    {children}
                </div>
            </main>
        </>
    );
}
