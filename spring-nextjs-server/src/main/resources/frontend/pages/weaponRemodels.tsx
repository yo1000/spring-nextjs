import {useEffect, useState} from "react";
import Content from "@/components/Content";
import Table from "@/components/Table";
import {useAuth} from "@/context/AuthContext";

type WeaponRemodel = {
    id: number;
    name: string;
    price: number;
    weapon: any;
    materials: any[];
};

type PagedData = {
    content: WeaponRemodel[];
};

const WeaponRemodels = () => {
    const {user} = useAuth();
    const [weaponRemodels, setWeaponRemodels] = useState<PagedData | null>();

    useEffect(() => {
        if (!user?.access_token) {
            return;
        }

        const fetchWeaponRemodels = async () => {
            const resp = await fetch(`http://localhost:8080/weaponRemodels`, {
                method: `GET`,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${user?.access_token}`,
                },
                body: null,
            });

            if (resp.ok) {
                setWeaponRemodels(await resp.json());
            } else {
                console.error(resp.status);
            }
        };

        try {
            void fetchWeaponRemodels();
        } catch (e) {
            console.error(e);
        }
    }, [user?.profile?.preferred_username]);

    return (
        <Content title={`Weapon Remodels`}>
            <Table
                data={{
                    ...weaponRemodels,
                    content: weaponRemodels?.content?.map(datum => ({
                        id: datum.id,
                        name: datum.weapon.name,
                        price: datum.price,
                        materials: datum.materials?.map(material => `${material.item.name}: ${material.quantity}`).join(", "),
                    }))
                }}
                onSearch={async (v) => {
                    const resp = await fetch(`http://localhost:8080/weaponRemodels?weaponName=${encodeURIComponent(v)}`, {
                        method: `GET`,
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": `Bearer ${user?.access_token}`,
                        },
                        body: null,
                    });

                    if (resp.ok) {
                        setWeaponRemodels(await resp.json());
                    } else {
                        console.error(resp.status);
                    }
                }}
            />
        </Content>
    );
}

export default WeaponRemodels;
