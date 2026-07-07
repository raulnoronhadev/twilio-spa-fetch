// Single owner of the persisted session (localStorage). Used by both the API
// client (attach/expire token) and the auth slice (login/logout/rehydrate).

const TOKEN_KEY = 'twilio_spa_token';
const ACCOUNT_SID_KEY = 'twilio_spa_account_sid';
const ACCOUNT_NAME_KEY = 'twilio_spa_account_name';

export interface StoredSession {
    token: string;
    accountSid: string | null;
    accountName: string | null;
}

export function getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
}

export function getStoredSession(): StoredSession | null {
    const token = getToken();
    if (token === null) return null;
    return {
        token,
        accountSid: localStorage.getItem(ACCOUNT_SID_KEY),
        accountName: localStorage.getItem(ACCOUNT_NAME_KEY),
    };
}

export function saveSession(session: { token: string; accountSid: string; accountName: string }): void {
    localStorage.setItem(TOKEN_KEY, session.token);
    localStorage.setItem(ACCOUNT_SID_KEY, session.accountSid);
    localStorage.setItem(ACCOUNT_NAME_KEY, session.accountName);
}

export function clearSession(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ACCOUNT_SID_KEY);
    localStorage.removeItem(ACCOUNT_NAME_KEY);
}
