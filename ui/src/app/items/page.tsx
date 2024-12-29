'use client';

import {useEffect, useState} from "react";
import Content from "@/components/Content";
import Table, {PagedData} from "@/components/Table";
import {useAuth} from "@/contexts/AuthContext";
import ItemsApiClient from "@/utils/ItemsApiClient";

const Items = () => {
    const {user} = useAuth();
    const itemsApiClient = new ItemsApiClient(user?.access_token);

    const [items, setItems] = useState<PagedData<any> | null>();

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
    }, [user?.profile?.preferred_username]);

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
