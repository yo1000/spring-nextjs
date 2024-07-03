'use client';

import {useEffect, useState} from "react";
import Content from "@/app/components/Content";
import Table, {PagedData} from "@/app/components/Table";
import {useAuth} from "@/app/contexts/AuthContext";
import Fetch from "@/app/utils/Fetch";

const Weapons = () => {
    const {user} = useAuth();
    const [weapons, setWeapons] = useState<PagedData<any> | null>();
    const [fetch, setFetch] = useState(new Fetch(user?.access_token));

    useEffect(() => {
        setFetch(new Fetch(user?.access_token));
    }, [user?.access_token]);

    useEffect(() => {
        const fetchWeapons = async () => {
            try {
                setWeapons(await fetch.to(`http://localhost:8080/weapons`));
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
                        setWeapons(await fetch.to(`http://localhost:8080/weapons?name=${encodeURIComponent(v)}`));
                    } catch (e) {
                        console.error(e);
                    }
                }}
                onClickPage={async (keyword, page) => {
                    try {
                        setWeapons(await fetch.to(`http://localhost:8080/weapons?name=${encodeURIComponent(keyword)}&page=${page}`));
                    } catch (e) {
                        console.error(e);
                    }
                }}
            />
        </Content>
    );
}

export default Weapons;
