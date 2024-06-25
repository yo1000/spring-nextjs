import {useEffect, useState} from "react";
import Content from "@/components/Content";
import Table from "@/components/Table";
import {useAuth} from "@/context/AuthContext";

type PagedData = {
    content: any[];
};

const Weapons = () => {
    const {user} = useAuth();
    const [weapons, setWeapons] = useState<PagedData | null>();

    useEffect(() => {
        if (!user?.access_token) {
            return;
        }

        const fetchWeapons = async () => {
            const resp = await fetch(`http://localhost:8080/weapons`, {
                method: `GET`,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${user?.access_token}`,
                },
                body: null,
            });

            if (resp.ok) {
                setWeapons(await resp.json());
            } else {
                console.error(resp.status);
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
                onSearch={async (v) => {
                    const resp = await fetch(`http://localhost:8080/weapons?name=${encodeURIComponent(v)}`, {
                        method: `GET`,
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": `Bearer ${user?.access_token}`,
                        },
                        body: null,
                    });

                    if (resp.ok) {
                        setWeapons(await resp.json());
                    } else {
                        console.error(resp.status);
                    }
                }}/>
        </Content>
    );
}

export default Weapons;
