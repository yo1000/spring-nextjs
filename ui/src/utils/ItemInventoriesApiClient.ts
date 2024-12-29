'use client';

import ApiClient from "@/utils/ApiClient";
import {PagedData} from "@/components/Table";

export type ItemInventory = {
    id: number;
    item: any;
    quantity: number;
};

export default class ItemInventoriesApiClient {
    private readonly apiClient: ApiClient

    constructor(accessToken?: string) {
        this.apiClient = new ApiClient(accessToken);
    }

    public async get(page?: number): Promise<PagedData<ItemInventory> | null> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/itemInventories${page ? `?page=${page}` : ``}`);
    }

    public async getById(id: number): Promise<ItemInventory | null> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/itemInventories/${id}`);
    }

    public async getByItemName(itemName: string, page?: number): Promise<PagedData<ItemInventory> | null> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/itemInventories?itemName=${encodeURIComponent(itemName)}${page ? `&page=${page}` : ``}`);
    }

    public async post(itemInventory: ItemInventory): Promise<ItemInventory> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/itemInventories`, {
                method: `POST`,
                body: JSON.stringify(itemInventory),
            });
    }

    public async patchById(id: number, itemInventory: ItemInventory): Promise<ItemInventory> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/itemInventories/${id}`, {
                method: `PATCH`,
                headers: {
                    "Content-Type": "application/merge-patch+json",
                },
                body: JSON.stringify(itemInventory),
            });
    }
}
