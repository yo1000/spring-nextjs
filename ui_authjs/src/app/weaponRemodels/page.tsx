'use client';

import {useEffect, useMemo, useState} from "react";
import Content from "@/components/Content";
import Table, {PagedData} from "@/components/Table";
import WeaponRemodelsApiClient, {WeaponRemodel} from "@/utils/WeaponRemodelsApiClient";
import {useSession} from "next-auth/react";
import {User} from "next-auth";

type WeaponRemodelData = {
    id: number;
    name: string;
    price: number;
    materials: string[];
};

const WeaponRemodels = () => {
    const apiBaseUri = process.env.NEXT_PUBLIC_API_BASE_URI;

    const { data } = useSession();
    const user: User | undefined = data?.user;
    const accessToken = data?.accessToken;

    const [weaponRemodels, setWeaponRemodels] = useState<PagedData<WeaponRemodel> | null>();

    const weaponRemodelsApiClient = useMemo(
        () => new WeaponRemodelsApiClient(accessToken, apiBaseUri),
        [accessToken, apiBaseUri]
    );

    useEffect(() => {
        const fetchWeaponRemodels = async () => {
            try {
                setWeaponRemodels(await weaponRemodelsApiClient.get());
            } catch (e) {
                console.error(e);
            }
        };

        try {
            void fetchWeaponRemodels();
        } catch (e) {
            console.error(e);
        }
    }, [user]);

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
                        setWeaponRemodels(await weaponRemodelsApiClient.getByWeaponName(v));
                    } catch (e) {
                        console.error(e);
                    }
                }}
                onClickPage={async (keyword, page) => {
                    try {
                        setWeaponRemodels(await weaponRemodelsApiClient.getByWeaponName(keyword, page));
                    } catch (e) {
                        console.error(e);
                    }
                }}
            />
        </Content>
    );
}

export default WeaponRemodels;
