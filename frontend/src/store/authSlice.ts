import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import { getToken, setToken, clearToken } from '../services/api';

interface AuthState {
  userIsLogged: boolean;
  accountSid: string | null;
  accountName: string | null;
}

const ACCOUNT_SID_KEY = 'twilio_spa_account_sid';
const ACCOUNT_NAME_KEY = 'twilio_spa_account_name';

// Rehydrate the session from localStorage so a page refresh keeps the user logged in.
const storedToken = getToken();

const initialState: AuthState = {
  userIsLogged: storedToken !== null,
  accountSid: storedToken ? localStorage.getItem(ACCOUNT_SID_KEY) : null,
  accountName: storedToken ? localStorage.getItem(ACCOUNT_NAME_KEY) : null,
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
      setToken(action.payload.token);
      localStorage.setItem(ACCOUNT_SID_KEY, action.payload.accountSid);
      localStorage.setItem(ACCOUNT_NAME_KEY, action.payload.accountName);
    },
    logout(state) {
      state.userIsLogged = false;
      state.accountSid = null;
      state.accountName = null;
      clearToken();
      localStorage.removeItem(ACCOUNT_SID_KEY);
      localStorage.removeItem(ACCOUNT_NAME_KEY);
    },
  },
});

export const { loginSuccess, logout } = authSlice.actions;
export default authSlice.reducer;
