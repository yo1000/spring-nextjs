'use client';

import {useEffect, useState} from "react";
import Content from "@/components/Content";
import Table, {PagedData} from "@/components/Table";
import {useAuth} from "@/contexts/AuthContext";
import WeaponsApiClient from "@/utils/WeaponsApiClient";

const Weapons = () => {
    const apiBaseUri = process.env.NEXT_PUBLIC_API_BASE_URI;

    const {user} = useAuth();
    const weaponsApiClient = new WeaponsApiClient(user?.access_token, apiBaseUri);

    const [weapons, setWeapons] = useState<PagedData<any> | null>();

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
    }, [user?.profile?.preferred_username]);

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
