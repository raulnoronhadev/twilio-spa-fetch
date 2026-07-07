import { apiRequest } from './api';

export interface LoginCredentials {
    accountSid: string;
    authToken: string;
}

export interface LoginResponse {
    token: string;
    accountSid: string;
    accountName: string;
    expireIn: number;
}

export async function loginRequest(credentials: LoginCredentials): Promise<LoginResponse> {
    return apiRequest<LoginResponse>('/api/v1/auth/login', {
        method: 'POST',
        body: JSON.stringify(credentials),
    });
}

export async function logoutRequest(): Promise<{ message: string }> {
    return apiRequest<{ message: string }>('/api/v1/auth/logout', { method: 'POST' });
}
