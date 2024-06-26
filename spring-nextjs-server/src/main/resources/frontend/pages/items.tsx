import {useEffect, useState} from "react";
import Content from "@/components/Content";
import Table, {PagedData} from "@/components/Table";
import {useAuth} from "@/context/AuthContext";

const Items = () => {
    const {user} = useAuth();
    const [items, setItems] = useState<PagedData<any> | null>();

    useEffect(() => {
        if (!user?.access_token) {
            return;
        }

        const fetchItems = async () => {
            const resp = await fetch(`http://localhost:8080/items`, {
                method: `GET`,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${user?.access_token}`,
                },
                body: null,
            });

            if (resp.ok) {
                setItems(await resp.json());
            } else {
                console.error(resp.status);
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
                    const resp = await fetch(`http://localhost:8080/items?name=${encodeURIComponent(v)}`, {
                        method: `GET`,
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": `Bearer ${user?.access_token}`,
                        },
                        body: null,
                    });

                    if (resp.ok) {
                        setItems(await resp.json());
                    } else {
                        console.error(resp.status);
                    }
                }}
                onClickPage={async (keyword, page) => {
                    const resp = await fetch(`http://localhost:8080/items?name=${encodeURIComponent(keyword)}&page=${page}`, {
                        method: `GET`,
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": `Bearer ${user?.access_token}`,
                        },
                        body: null,
                    });

                    if (resp.ok) {
                        setItems(await resp.json());
                    } else {
                        console.error(resp.status);
                    }
                }}
            />
        </Content>
    );
}

export default Items;
