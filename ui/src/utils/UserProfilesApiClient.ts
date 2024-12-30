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
    private readonly baseUri: string

    constructor(accessToken: string | undefined, baseUri: string | undefined) {
        this.apiClient = new ApiClient(accessToken);
        this.baseUri = baseUri ?? ``;
    }

    public async get(page?: number): Promise<PagedData<UserProfile> | undefined> {
        return await this.apiClient.fetchTo(
            `${this.baseUri}/userProfiles${page ? `?page=${page}` : ``}`);
    }

    public async getByUsername(username: string, page?: number): Promise<PagedData<UserProfile> | undefined> {
        return await this.apiClient.fetchTo(
            `${this.baseUri}/userProfiles?username=${encodeURIComponent(username)}${page ? `&page=${page}` : ``}`);
    }

    public async post(userProfile: UserProfile): Promise<UserProfile> {
        return await this.apiClient.fetchTo(
            `${this.baseUri}/userProfiles`, {
                method: `POST`,
                body: JSON.stringify(userProfile),
            });
    }

    public async patchByUsername(username: string, userProfile: UserProfile): Promise<UserProfile> {
        return await this.apiClient.fetchTo(
            `${this.baseUri}/userProfiles?username=${encodeURIComponent(username)}`, {
                method: `PATCH`,
                headers: {
                    "Content-Type": "application/merge-patch+json",
                },
                body: JSON.stringify(userProfile),
            });
    }
}
