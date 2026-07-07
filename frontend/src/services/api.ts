const BASE_URL = 'http://localhost:8080';

const TOKEN_KEY = 'twilio_spa_token';

export function getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
}

export function setToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
}

export function clearToken(): void {
    localStorage.removeItem(TOKEN_KEY);
}

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
