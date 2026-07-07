import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import { getStoredSession, saveSession, clearSession } from '../services/session';

interface AuthState {
  userIsLogged: boolean;
  accountSid: string | null;
  accountName: string | null;
}

// Rehydrate the session from localStorage so a page refresh keeps the user logged in.
const stored = getStoredSession();

const initialState: AuthState = {
  userIsLogged: stored !== null,
  accountSid: stored?.accountSid ?? null,
  accountName: stored?.accountName ?? null,
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    loginSuccess(
      state,
      action: PayloadAction<{ token: string; accountSid: string; accountName: string }>
    ) {
      state.userIsLogged = true;
      state.accountSid = action.payload.accountSid;
      state.accountName = action.payload.accountName;
      saveSession(action.payload);
    },
    logout(state) {
      state.userIsLogged = false;
      state.accountSid = null;
      state.accountName = null;
      clearSession();
    },
  },
});

export const { loginSuccess, logout } = authSlice.actions;
export default authSlice.reducer;
