import {useEffect, useState} from "react";
import Content from "@/components/Content";
import Table from "@/components/Table";
import {useAuth} from "@/context/AuthContext";
import Modal from "@/components/Modal";

type ItemInventory = {
    id: number;
    item: any;
    quantity: number;
};

type PagedData = {
    content: ItemInventory[];
};

const ItemInventories = () => {
    const {user} = useAuth();
    const [itemInventories, setItemInventories] = useState<PagedData | null>();
    const [editModalShown, setEditModalShown] = useState(false);
    const [editModalData, setEditModalData] = useState<any | null | undefined>(null);
    const [addModalShown, setAddModalShown] = useState(false);
    const [addModalData, setAddModalData] = useState<any | null | undefined>(null);

    useEffect(() => {
        if (!user?.access_token) {
            return;
        }

        const fetchItemInventories = async () => {
            const resp = await fetch(`http://localhost:8080/itemInventories`, {
                method: `GET`,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${user?.access_token}`,
                },
                body: null,
            });

            if (resp.ok) {
                setItemInventories(await resp.json());
            } else {
                console.error(resp.status);
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
                }}
                searchLabel={`Name`}
                onSearch={async (v) => {
                    const resp = await fetch(`http://localhost:8080/itemInventories?itemName=${encodeURIComponent(v)}`, {
                        method: `GET`,
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": `Bearer ${user?.access_token}`,
                        },
                        body: null,
                    });

                    if (resp.ok) {
                        setItemInventories(await resp.json());
                    } else {
                        console.error(resp.status);
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

                       const patchResp = await fetch(`http://localhost:8080/itemInventories/${newContentItem.id}`, {
                           method: `PATCH`,
                           headers: {
                               "Content-Type": "application/merge-patch+json",
                               "Authorization": `Bearer ${user?.access_token}`,
                           },
                           body: JSON.stringify(newContentItem),
                       });

                       if (!patchResp.ok) {
                           return;
                       }

                       setItemInventories({
                           ...itemInventories,
                           content: [
                               ...keepContent,
                               newContentItem,
                           ]
                       });

                       setEditModalShown(false);
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

                       const getResp = await fetch(`http://localhost:8080/items/${data.itemId}`, {
                           method: `GET`,
                           headers: {
                               "Content-Type": "application/json",
                               "Authorization": `Bearer ${user?.access_token}`,
                           },
                           body: null,
                       });

                       if (!getResp.ok) {
                           return;
                       }

                       const item = await getResp.json();

                       const postResp = await fetch(`http://localhost:8080/itemInventories`, {
                           method: `POST`,
                           headers: {
                               "Content-Type": "application/json",
                               "Authorization": `Bearer ${user?.access_token}`,
                           },
                           body: JSON.stringify({
                               id: data.id,
                               item: item,
                               quantity: data.quantity,
                           }),
                       });

                       if (!postResp.ok) {
                           return;
                       }

                       setItemInventories({
                           ...itemInventories,
                           content: [
                               ...(itemInventories?.content ?? []),
                               {
                                   id: data.id,
                                   item: item,
                                   quantity: data.quantity,
                               },
                           ]
                       });

                       setAddModalShown(false);
                   }}
                   onCancel={() => {
                       setAddModalShown(false);
                   }}
            />
        </Content>
    );
}

export default ItemInventories;
