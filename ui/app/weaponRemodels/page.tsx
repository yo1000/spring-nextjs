'use client';

import {useEffect, useState} from "react";
import Content from "@/app/src/components/Content";
import Table, {PagedData} from "@/app/src/components/Table";
import {useAuth} from "@/app/src/contexts/AuthContext";
import Fetch from "@/app/src/utils/Fetch";

type WeaponRemodel = {
    id: number;
    price: number;
    weapon: any;
    materials: any[];
};

type WeaponRemodelData = {
    id: number;
    name: string;
    price: number;
    materials: string[];
};

const WeaponRemodels = () => {
    const {user} = useAuth();
    const [weaponRemodels, setWeaponRemodels] = useState<PagedData<WeaponRemodel> | null>();
    const [fetch, setFetch] = useState(new Fetch(user?.access_token));

    useEffect(() => {
        setFetch(new Fetch(user?.access_token));
    }, [user?.access_token]);

    useEffect(() => {
        const fetchWeaponRemodels = async () => {
            try {
                setWeaponRemodels(await fetch.to(`http://localhost:8080/weaponRemodels`));
            } catch (e) {
                console.error(e);
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
                } as PagedData<WeaponRemodelData>}
                searchLabel={`Name`}
                onSearch={async (v) => {
                    try {
                        setWeaponRemodels(await fetch.to(`http://localhost:8080/weaponRemodels?weaponName=${encodeURIComponent(v)}`));
                    } catch (e) {
                        console.error(e);
                    }
                }}
                onClickPage={async (keyword, page) => {
                    try {
                        setWeaponRemodels(await fetch.to(`http://localhost:8080/weaponRemodels?weaponName=${encodeURIComponent(keyword)}&page=${page}`));
                    } catch (e) {
                        console.error(e);
                    }
                }}
            />
        </Content>
    );
}

export default WeaponRemodels;
