import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { useAppDispatch } from './reduxHooks';
import { loginSuccess } from '../store/authSlice';
import { loginRequest } from '../services/authService';

export function useLogin() {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    return useMutation({
        mutationFn: loginRequest,
        onSuccess: (_, variables) => {
            dispatch(loginSuccess({ accountSid: variables.accountSid }));
            navigate('/');
        },
        onError: (error) => {
            console.error('Login failed:', error);
        },
    });
}