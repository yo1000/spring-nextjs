'use client';

import ApiClient from "@/utils/ApiClient";
import {PagedData} from "@/components/Table";

export type UserProfile = {
    id: number;
    username: string;
    familyName: string;
    givenName: string;
    age: number;
    gender: string;
    profile: string;
};

export default class UserProfilesApiClient {
    private readonly apiClient: ApiClient

    constructor(accessToken?: string) {
        this.apiClient = new ApiClient(accessToken);
    }

    public async get(page?: number): Promise<PagedData<UserProfile> | null> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/userProfiles${page ? `?page=${page}` : ``}`);
    }

    public async getByUsername(username: string, page?: number): Promise<PagedData<UserProfile> | null> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/userProfiles?username=${encodeURIComponent(username)}${page ? `&page=${page}` : ``}`);
    }

    public async post(userProfile: UserProfile): Promise<UserProfile> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/userProfiles`, {
                method: `POST`,
                body: JSON.stringify(userProfile),
            });
    }

    public async patchByUsername(username: string, userProfile: UserProfile): Promise<UserProfile> {
        return await this.apiClient.fetchTo(
            `http://localhost:8080/userProfiles?username=${encodeURIComponent(username)}`, {
                method: `PATCH`,
                headers: {
                    "Content-Type": "application/merge-patch+json",
                },
                body: JSON.stringify(userProfile),
            });
    }
}
