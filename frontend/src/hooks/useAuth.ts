import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from './reduxHooks';
import { loginSuccess, logout } from '../store/authSlice';
import { loginRequest, logoutRequest } from '../services/authService';

export function useLogin() {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    return useMutation({
        mutationFn: loginRequest,
        onSuccess: (data) => {
            dispatch(loginSuccess({
                token: data.token,
                accountSid: data.accountSid,
                accountName: data.accountName,
            }));
            navigate('/');
        },
        onError: (error) => {
            console.error('Login failed:', error);
        },
    });
}

export function useLogout() {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: logoutRequest,
        onSettled: () => {
            // Clear local session even if the server call fails.
            dispatch(logout());
            queryClient.clear();
            navigate('/login');
        },
    });
}

export function useAuth() {
    return useAppSelector((state) => state.auth);
}
