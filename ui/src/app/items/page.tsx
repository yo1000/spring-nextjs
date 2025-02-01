'use client';

import {useEffect, useMemo, useState} from "react";
import Content from "@/components/Content";
import Table, {PagedData} from "@/components/Table";
import {useAuth} from "@/contexts/AuthContext";
import ItemsApiClient from "@/utils/ItemsApiClient";

const Items = () => {
    const apiBaseUri = process.env.NEXT_PUBLIC_API_BASE_URI;

    const {user} = useAuth();

    const [items, setItems] = useState<PagedData<any> | null>();

    const itemsApiClient = useMemo(
        () => new ItemsApiClient(user?.access_token, apiBaseUri),
        [user?.access_token, apiBaseUri]);

    useEffect(() => {
        const fetchItems = async () => {
            try {
                setItems(await itemsApiClient.get());
            } catch (e) {
                console.error(e);
            }
        };

        try {
            void fetchItems();
        } catch (e) {
            console.error(e);
        }
    }, [user?.profile?.preferred_username, itemsApiClient]);

    return (
        <Content title={`Items`}>
            <Table
                data={items}
                searchLabel={`Name`}
                onSearch={async (v) => {
                    try {
                        setItems(await itemsApiClient.getByName(v));
                    } catch (e) {
                        console.error(e);
                    }

                }}
                onClickPage={async (keyword, page) => {
                    try {
                        setItems(await itemsApiClient.getByName(keyword, page));
                    } catch (e) {
                        console.error(e);
                    }
                }}
            />
        </Content>
    );
}

export default Items;
