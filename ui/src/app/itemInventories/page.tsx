'use client';

import {useEffect, useState} from "react";
import Content from "@/components/Content";
import Table, {PagedData} from "@/components/Table";
import {useAuth} from "@/contexts/AuthContext";
import Modal from "@/components/Modal";
import ItemInventoriesApiClient, {ItemInventory} from "@/utils/ItemInventoriesApiClient";
import ItemsApiClient from "@/utils/ItemsApiClient";

type ItemInventoryData = {
    id: number;
    itemId: number;
    name: string;
    quantity: number;
};

const ItemInventories = () => {
    const apiBaseUri = process.env.NEXT_PUBLIC_API_BASE_URI;

    const {user} = useAuth();
    const itemsApiClient = new ItemsApiClient(user?.access_token, apiBaseUri);
    const itemInventoriesApiClient = new ItemInventoriesApiClient(user?.access_token, apiBaseUri);

    const [itemInventories, setItemInventories] = useState<PagedData<ItemInventory> | null>();
    const [editModalShown, setEditModalShown] = useState(false);
    const [editModalData, setEditModalData] = useState<any | null | undefined>(null);
    const [addModalShown, setAddModalShown] = useState(false);
    const [addModalData, setAddModalData] = useState<any | null | undefined>(null);

    useEffect(() => {
        const fetchItemInventories = async () => {
            try {
                setItemInventories(await itemInventoriesApiClient.get());
            } catch (e) {
                console.error(e);
            }
        };

        try {
            void fetchItemInventories();
        } catch (e) {
            console.error(e);
        }
    }, [user?.profile?.preferred_username]);

    return (
        <Content title={`Item Inventories`}>
            <Table
                data={{
                    ...itemInventories,
                    content: itemInventories?.content
                        ?.map(datum => ({
                            id: datum.id,
                            itemId: datum.item.id,
                            name: datum.item.name,
                            quantity: datum.quantity,
                        }))
                        ?.sort((a, b) => (a.id - b.id))
                } as PagedData<ItemInventoryData>}
                searchLabel={`Name`}
                onSearch={async (v) => {
                    try {
                        setItemInventories(await itemInventoriesApiClient.getByItemName(v));
                    } catch (e) {
                        console.error(e);
                    }
                }}
                onAdd={() => {
                    setAddModalData({
                        id: ``,
                        itemId: ``,
                        name: ``,
                        quantity: ``,
                    });

                    setAddModalShown(true);
                }}
                onEdit={(data) => {
                    setEditModalData(data);

                    setEditModalShown(true);
                }}
                onClickPage={async (keyword, page) => {
                    try {
                        setItemInventories(await itemInventoriesApiClient.getByItemName(keyword, page));
                    } catch (e) {
                        console.error(e);
                    }
                }}
            />
            <Modal open={editModalShown}
                   data={editModalData}
                   readonly={[`id`, `itemId`, `name`]}
                   title={`Edit Item Inventory`}
                   onSave={async (data) => {
                       const keepContent = (itemInventories?.content ?? [])
                           .filter(d => data.id !== d.id);
                       const newContentItem = {
                           ...(((itemInventories?.content ?? [])).find(d => data.id === d.id)!),
                           quantity: data.quantity,
                       }

                       if (!newContentItem) {
                           return;
                       }

                       try {
                           await itemInventoriesApiClient.patchById(newContentItem.id, newContentItem);

                           setItemInventories({
                               ...itemInventories,
                               content: [
                                   ...keepContent,
                                   newContentItem,
                               ]
                           } as PagedData<ItemInventory>);

                           setEditModalShown(false);
                       } catch (e) {
                           console.error(e);
                       }
                   }}
                   onCancel={() => {
                       setEditModalShown(false);
                   }}
            />
            <Modal open={addModalShown}
                   data={addModalData}
                   title={`Add Item Inventory`}
                   onSave={async (data) => {
                       if (!data.itemId) {
                           return;
                       }

                       try {
                           const item = await itemsApiClient.getById(data.itemId);

                           const newData = await itemInventoriesApiClient.post({
                               id: data.id,
                               item: item,
                               quantity: data.quantity,
                           });

                           setItemInventories({
                               ...itemInventories,
                               content: [
                                   ...(itemInventories?.content ?? []),
                                   {
                                       id: newData.id,
                                       item: item,
                                       quantity: newData.quantity,
                                   },
                               ]
                           } as PagedData<ItemInventory>);

                           setAddModalShown(false);
                       } catch (e) {
                           console.error(e);
                       }
                   }}
                   onCancel={() => {
                       setAddModalShown(false);
                   }}
            />
        </Content>
    );
}

export default ItemInventories;
