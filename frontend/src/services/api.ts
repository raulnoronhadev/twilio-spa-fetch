import { getToken, clearSession } from './session';

const BASE_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080';

export class ApiError extends Error {
    status: number;

    constructor(status: number, message: string) {
        super(message);
        this.status = status;
    }
}

export async function apiRequest<T>(
    endpoint: string,
    options?: RequestInit
): Promise<T> {
    const token = getToken();

    const response = await fetch(`${BASE_URL}${endpoint}`, {
        ...options,
        headers: {
            'Content-Type': 'application/json',
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
            ...options?.headers,
        },
    });

    // A 401 while holding a token means the session expired (or was revoked):
    // clear it and send the user back to the login screen. A 401 without a
    // token is a failed login attempt and must surface as a normal error.
    if (response.status === 401 && token !== null) {
        clearSession();
        window.location.assign('/login');
        throw new ApiError(401, 'Session expired');
    }

    if (!response.ok) {
        let message = `HTTP error: ${response.status}`;
        try {
            const body = await response.json();
            message = body.message ?? body.error ?? message;
        } catch {
            // response body was not JSON — keep the default message
        }
        throw new ApiError(response.status, message);
    }

    const text = await response.text();
    try {
        return JSON.parse(text) as T;
    } catch {
        // some endpoints (e.g. backup) return plain strings
        return text as unknown as T;
    }
}
