'use client';

export class FetchError extends Error {
    readonly reason?: any;

    constructor(message: string, reason?: any) {
        super(message);
        this.reason = reason;
    }
}

export default class Fetch {
    private readonly accessToken?: string;

    constructor(accessToken?: string) {
        this.accessToken = accessToken;
    }

    public async to(uri: string, options?: any) {
        console.log(this.accessToken);

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
