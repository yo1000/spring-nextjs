import {Disclosure, DisclosureButton, DisclosurePanel} from '@headlessui/react';
import {Bars3Icon, XMarkIcon} from '@heroicons/react/24/outline';
import React, {ReactNode, useEffect, useState} from "react";
import {useRouter} from "next/router";
import {useAuth} from "@/contexts/AuthContext";
import Link from "next/link";
import Image from "next/image";

type LayoutProps = {
    children: ReactNode;
};

export default function Layout({ children }: LayoutProps) {
    const {user, signinRedirect, signoutRedirect} = useAuth();

    const router = useRouter();
    const [pathname, setPathname] = useState('');

    const navigation = [
        { name: 'Showcase', href: `/showcase/`, protected: false },
        { name: 'Items', href: `/items/`, protected: false },
        { name: 'Item Inventories', href: `/itemInventories/`, protected: true },
        { name: 'Weapons', href: `/weapons/`, protected: true },
        { name: 'Weapon Remodels', href: `/weaponRemodels/`, protected: true },
    ];

    function classNames(...classes: string[]) {
        return classes.filter(Boolean).join(' ')
    }

    useEffect(() => {
        if (typeof window !== 'undefined') {
            setPathname(window.location.pathname);
        }
    }, [router]);

    return (
        <>
            <div className="min-h-full">
                <Disclosure as="nav" className="bg-gray-800">
                    {({ open }) => (
                        <>
                            <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                                <div className="flex h-16 items-center justify-between">
                                    <div className="flex items-center">
                                        <div className="flex-shrink-0">
                                            <Link href={`/`}>
                                                <Image
                                                    className="h-fit w-fit text-white"
                                                    width={56}
                                                    height={28}
                                                    src={`${router.basePath}/logo.svg`}
                                                    alt="Showcase logo"
                                                />
                                            </Link>
                                        </div>
                                        <div className="hidden md:block">
                                            <div className="ml-10 flex items-baseline space-x-4">
                                                {navigation.map((item) => (
                                                    <Link key={item.name}
                                                          href={item.href}
                                                          className={classNames(
                                                              item.href === pathname
                                                                  ? 'bg-gray-900 text-white'
                                                                  : 'text-gray-300 hover:bg-gray-700 hover:text-white',
                                                              'rounded-md px-3 py-2 text-sm font-medium',
                                                          )}
                                                          aria-current={item.href === pathname ? 'page' : undefined}>
                                                        {item.name}{item.protected ? (
                                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16"
                                                             fill="currentColor" className="inline ml-0.5 pb-0.5 size-4">
                                                            <path fillRule="evenodd"
                                                                  d="M8 1a3.5 3.5 0 0 0-3.5 3.5V7A1.5 1.5 0 0 0 3 8.5v5A1.5 1.5 0 0 0 4.5 15h7a1.5 1.5 0 0 0 1.5-1.5v-5A1.5 1.5 0 0 0 11.5 7V4.5A3.5 3.5 0 0 0 8 1Zm2 6V4.5a2 2 0 1 0-4 0V7h4Z" clipRule="evenodd" />
                                                        </svg>
                                                    ) : (<></>)}
                                                    </Link>
                                                ))}
                                            </div>
                                        </div>
                                    </div>
                                    <div className="hidden md:block">
                                        <div className="ml-4 flex items-center md:ml-6">
                                            <div className={`relative ml-3 space-x-4`}>
                                                {user?.profile?.preferred_username
                                                    ? (<>
                                                        <span
                                                            className={`text-gray-300 rounded-md px-3 py-2 text-sm font-medium`}>
                                                            {user?.profile?.preferred_username}
                                                        </span>
                                                        <button
                                                            className={`text-gray-300 hover:bg-gray-700 hover:text-white rounded-md px-3 py-2 text-sm font-medium ring-1 ring-inset ring-gray-300`}
                                                            onClick={signoutRedirect}>
                                                            Sign out
                                                        </button>
                                                    </>)
                                                    : (<button
                                                        className={`text-gray-300 hover:bg-gray-700 hover:text-white rounded-md px-3 py-2 text-sm font-medium ring-1 ring-inset ring-gray-300`}
                                                        onClick={signinRedirect}>
                                                        Sign in
                                                    </button>)
                                                }
                                            </div>
                                        </div>
                                    </div>
                                    <div className="-mr-2 flex md:hidden">
                                        {/* Mobile menu button */}
                                        <DisclosureButton
                                            className="relative inline-flex items-center justify-center rounded-md bg-gray-800 p-2 text-gray-400 hover:bg-gray-700 hover:text-white focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800">
                                            <span className="absolute -inset-0.5"/>
                                            <span className="sr-only">Open main menu</span>
                                            {open ? (
                                                <XMarkIcon className="block h-6 w-6" aria-hidden="true"/>
                                            ) : (
                                                <Bars3Icon className="block h-6 w-6" aria-hidden="true"/>
                                            )}
                                        </DisclosureButton>
                                    </div>
                                </div>
                            </div>

                            <DisclosurePanel className="md:hidden">
                                <div className="space-y-1 px-2 pb-3 pt-2 sm:px-3">
                                    {navigation.map((item) => (
                                        <DisclosureButton
                                            key={item.name}
                                            as="a"
                                            href={item.href}
                                            className={classNames(
                                                item.href === pathname
                                                    ? 'bg-gray-900 text-white'
                                                    : 'text-gray-300 hover:bg-gray-700 hover:text-white',
                                                'block rounded-md px-3 py-2 text-base font-medium',
                                            )}
                                            aria-current={item.href === pathname ? 'page' : undefined}
                                        >
                                            {item.name}{item.protected ? (
                                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16"
                                                 fill="currentColor" className="inline ml-0.5 pb-0.5 size-4">
                                                <path fillRule="evenodd"
                                                      d="M8 1a3.5 3.5 0 0 0-3.5 3.5V7A1.5 1.5 0 0 0 3 8.5v5A1.5 1.5 0 0 0 4.5 15h7a1.5 1.5 0 0 0 1.5-1.5v-5A1.5 1.5 0 0 0 11.5 7V4.5A3.5 3.5 0 0 0 8 1Zm2 6V4.5a2 2 0 1 0-4 0V7h4Z" clipRule="evenodd" />
                                            </svg>
                                        ) : (<></>)}
                                        </DisclosureButton>
                                    ))}
                                </div>
                                <div className="border-t border-gray-700 pb-3 pt-4">
                                    <div className="space-y-1 px-2">
                                        {user?.profile?.preferred_username
                                            ? (<>
                                                <DisclosureButton as="span" className="block rounded-md px-3 py-2 text-base font-medium text-gray-400">
                                                    {user?.profile?.preferred_username}
                                                </DisclosureButton>
                                                <DisclosureButton as="span" onClick={signoutRedirect} className="block rounded-md px-3 py-2 text-base font-medium text-gray-400 hover:bg-gray-700 hover:text-white">
                                                    Sign out
                                                </DisclosureButton>
                                            </>)
                                            : (<DisclosureButton as="span" onClick={signinRedirect} className="block rounded-md px-3 py-2 text-base font-medium text-gray-400 hover:bg-gray-700 hover:text-white">
                                                    Sign in
                                            </DisclosureButton>)
                                        }
                                    </div>
                                </div>
                            </DisclosurePanel>
                        </>
                    )}
                </Disclosure>

                {children}
            </div>
        </>
    )
}
