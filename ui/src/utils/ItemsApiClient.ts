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

    constructor(accessToken?: string) {
        this.apiClient = new ApiClient(accessToken);
    }

    public async get(page?: number): Promise<PagedData<Item> | null> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/items${page ? `?page=${page}` : ``}`);
    }

    public async getById(id: number): Promise<Item | null> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/items/${id}`);
    }

    public async getByName(name: string, page?: number): Promise<PagedData<Item> | null> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/items?name=${encodeURIComponent(name)}${page ? `&page=${page}` : ``}`);
    }
}
