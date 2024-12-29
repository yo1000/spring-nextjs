'use client';

import ApiClient from "@/utils/ApiClient";
import {PagedData} from "@/components/Table";

export type WeaponRemodel = {
    id: number;
    price: number;
    weapon: any;
    materials: any[];
};

export default class WeaponRemodelsApiClient {
    private readonly apiClient: ApiClient

    constructor(accessToken?: string) {
        this.apiClient = new ApiClient(accessToken);
    }

    public async get(page?: number): Promise<PagedData<WeaponRemodel> | null> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/weaponRemodels${page ? `?page=${page}` : ``}`);
    }

    public async getById(id: number): Promise<WeaponRemodel | null> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/weaponRemodels/${id}`);
    }

    public async getByWeaponName(weaponName: string, page?: number): Promise<PagedData<WeaponRemodel> | null> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/weaponRemodels?weaponName=${encodeURIComponent(weaponName)}${page ? `&page=${page}` : ``}`);
    }
}
