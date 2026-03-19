import { createSlice, type PayloadAction } from '@reduxjs/toolkit';

interface AuthState {
  userIsLogged: boolean;
  accountSid: string | null;
}

const initialState: AuthState = {
  userIsLogged: false,
  accountSid: null,
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    loginSuccess(state, action: PayloadAction<{ accountSid: string }>) {
      state.userIsLogged = true;
      state.accountSid = action.payload.accountSid;
    },
    logout(state) {
      state.userIsLogged = false;
      state.accountSid = null;
    },
  },
});

export const { loginSuccess, logout } = authSlice.actions;
export default authSlice.reducer;