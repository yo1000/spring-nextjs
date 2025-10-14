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
    private readonly baseUri: string

    constructor(accessToken: string | undefined, baseUri: string | undefined) {
        this.apiClient = new ApiClient(accessToken);
        this.baseUri = baseUri ?? ``;
    }

    public async get(page?: number): Promise<PagedData<Weapon> | undefined> {
        return await this.apiClient.fetchTo(
            `${this.baseUri}/weapons${page ? `?page=${page}` : ``}`);
    }

    public async getById(id: number): Promise<Weapon | undefined> {
        return await this.apiClient.fetchTo(
            `${this.baseUri}/weapons/${id}`);
    }

    public async getByName(name: string, page?: number): Promise<PagedData<Weapon> | undefined> {
        return await this.apiClient.fetchTo(
            `${this.baseUri}/weapons?name=${encodeURIComponent(name)}${page ? `&page=${page}` : ``}`);
    }
}
