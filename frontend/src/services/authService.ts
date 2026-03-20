import { apiRequest } from './api';

interface LoginCredentials {
    accountSid: string;
    authToken: string;
}

interface LoginResponse {
    token: string;
}

export async function loginRequest(credentials: LoginCredentials): Promise<LoginResponse> {
    return apiRequest<LoginResponse>('/auth/login', {
        method: 'POST',
        body: JSON.stringify(credentials),
    });
}