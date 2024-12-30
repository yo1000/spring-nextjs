'use client';

export class FetchError extends Error {
    readonly reason?: any;

    constructor(message: string, reason?: any) {
        super(message);
        this.reason = reason;
    }
}

export default class ApiClient {
    private readonly accessToken: string | undefined;

    constructor(accessToken: string | undefined) {
        this.accessToken = accessToken;
    }

    public async fetchTo(uri: string, options?: any) {
        const authHeader = this.accessToken ? {"Authorization": `Bearer ${this.accessToken}`} : {};
        const headers = options && options.headers ? {
            "Content-Type": "application/json",
            ...(options.headers),
            ...authHeader,
        } : {
            "Content-Type": "application/json",
            ...authHeader,
        };

        const resp = await fetch(uri, {
            method: `GET`,
            ...options,
            headers: headers,
        });

        if (resp.ok) {
            return await resp.json();
        } else {
            throw new FetchError(resp.statusText, resp);
        }
    }
}
