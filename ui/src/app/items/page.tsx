'use client';

import {useEffect, useState} from "react";
import Content from "@/components/Content";
import Table, {PagedData} from "@/components/Table";
import {useAuth} from "@/contexts/AuthContext";
import Fetch from "@/utils/Fetch";

const Items = () => {
    const {user} = useAuth();
    const [items, setItems] = useState<PagedData<any> | null>();
    const [fetch, setFetch] = useState(new Fetch(user?.access_token));

    useEffect(() => {
        setFetch(new Fetch(user?.access_token));
    }, [user?.access_token]);

    useEffect(() => {
        const fetchItems = async () => {
            try {
                setItems(await fetch.to(`http://localhost:8080/items`));
            } catch (e) {
                console.error(e);
            }
        };

        try {
            void fetchItems();
        } catch (e) {
            console.error(e);
        }
    }, [user?.profile?.preferred_username]);

    return (
        <Content title={`Items`}>
            <Table
                data={items}
                searchLabel={`Name`}
                onSearch={async (v) => {
                    try {
                        setItems(await fetch.to(`http://localhost:8080/items?name=${encodeURIComponent(v)}`));
                    } catch (e) {
                        console.error(e);
                    }

                }}
                onClickPage={async (keyword, page) => {
                    try {
                        setItems(await fetch.to(`http://localhost:8080/items?name=${encodeURIComponent(keyword)}&page=${page}`));
                    } catch (e) {
                        console.error(e);
                    }
                }}
            />
        </Content>
    );
}

export default Items;
