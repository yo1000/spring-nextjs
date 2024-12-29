'use client';

import ApiClient from "@/utils/ApiClient";
import {PagedData} from "@/components/Table";

export type Weapon = {
    id: number;
    name: string;
    str: number;
    hit: number;
};

export default class WeaponsApiClient {
    private readonly apiClient: ApiClient

    constructor(accessToken?: string) {
        this.apiClient = new ApiClient(accessToken);
    }

    public async get(page?: number): Promise<PagedData<Weapon> | null> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/weapons${page ? `?page=${page}` : ``}`);
    }

    public async getById(id: number): Promise<Weapon | null> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/weapons/${id}`);
    }

    public async getByName(name: string, page?: number): Promise<PagedData<Weapon> | null> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/weapons?name=${encodeURIComponent(name)}${page ? `&page=${page}` : ``}`);
    }
}
