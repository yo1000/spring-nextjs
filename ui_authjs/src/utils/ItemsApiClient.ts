'use client';

import ApiClient from "@/utils/ApiClient";
import {PagedData} from "@/components/Table";

export type Item = {
    id: number;
    name: string;
    price?: number;
    sellPrice?: number;
};

export default class ItemsApiClient {
    private readonly apiClient: ApiClient
    private readonly baseUri: string

    constructor(accessToken: string | undefined, baseUri: string | undefined) {
        this.apiClient = new ApiClient(accessToken);
        this.baseUri = baseUri ?? ``;
    }

    public async get(page?: number): Promise<PagedData<Item> | undefined> {
        return await this.apiClient.fetchTo(
            `${this.baseUri}/items${page ? `?page=${page}` : ``}`);
    }

    public async getById(id: number): Promise<Item | undefined> {
        return await this.apiClient.fetchTo(
            `${this.baseUri}/items/${id}`);
    }

    public async getByName(name: string, page?: number): Promise<PagedData<Item> | undefined> {
        return await this.apiClient.fetchTo(
            `${this.baseUri}/items?name=${encodeURIComponent(name)}${page ? `&page=${page}` : ``}`);
    }
}
