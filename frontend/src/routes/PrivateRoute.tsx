import { Navigate, Outlet } from 'react-router-dom';
import { useAppSelector } from '../hooks/reduxHooks';

export default function PrivateRoute() {
    const userIsLogged = useAppSelector((state) => state.auth.userIsLogged);
    return userIsLogged ? <Outlet /> : <Navigate to="/login" replace />;
}