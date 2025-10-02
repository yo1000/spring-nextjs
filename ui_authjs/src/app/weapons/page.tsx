'use client';

import {useEffect, useMemo, useState} from "react";
import Content from "@/components/Content";
import Table, {PagedData} from "@/components/Table";
import WeaponsApiClient from "@/utils/WeaponsApiClient";
import {useSession} from "next-auth/react";
import {User} from "next-auth";

const Weapons = () => {
    const apiBaseUri = process.env.NEXT_PUBLIC_API_BASE_URI;

    const { data } = useSession();
    const user: User | undefined = data?.user;
    const accessToken = data?.accessToken;

    const [weapons, setWeapons] = useState<PagedData<any> | null>();

    const weaponsApiClient = useMemo(
        () => new WeaponsApiClient(accessToken, apiBaseUri),
        [accessToken, apiBaseUri]
    );

    useEffect(() => {
        const fetchWeapons = async () => {
            try {
                setWeapons(await weaponsApiClient.get());
            } catch (e) {
                console.error(e);
            }
        };

        try {
            void fetchWeapons();
        } catch (e) {
            console.error(e);
        }
    }, [user]);

    return (
        <Content title={`Weapons`}>
            <Table
                data={weapons}
                searchLabel={`Name`}
                onSearch={async (v) => {
                    try {
                        setWeapons(await weaponsApiClient.getByName(v));
                    } catch (e) {
                        console.error(e);
                    }
                }}
                onClickPage={async (keyword, page) => {
                    try {
                        setWeapons(await weaponsApiClient.getByName(keyword, page));
                    } catch (e) {
                        console.error(e);
                    }
                }}
            />
        </Content>
    );
}

export default Weapons;
