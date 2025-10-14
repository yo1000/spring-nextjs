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
    private readonly baseUri: string

    constructor(accessToken: string | undefined, baseUri: string | undefined) {
        this.apiClient = new ApiClient(accessToken);
        this.baseUri = baseUri ?? ``;
    }

    public async get(page?: number): Promise<PagedData<WeaponRemodel> | undefined> {
        return await this.apiClient.fetchTo(
            `${this.baseUri}/weaponRemodels${page ? `?page=${page}` : ``}`);
    }

    public async getById(id: number): Promise<WeaponRemodel | undefined> {
        return await this.apiClient.fetchTo(
            `${this.baseUri}/weaponRemodels/${id}`);
    }

    public async getByWeaponName(weaponName: string, page?: number): Promise<PagedData<WeaponRemodel> | undefined> {
        return await this.apiClient.fetchTo(
            `${this.baseUri}/weaponRemodels?weaponName=${encodeURIComponent(weaponName)}${page ? `&page=${page}` : ``}`);
    }
}
