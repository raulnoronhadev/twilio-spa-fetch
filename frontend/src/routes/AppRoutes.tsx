import { Routes, Route, Navigate } from 'react-router-dom';
import Login from '../pages/Login/Login';
import Home from '../pages/Home/Home';
import StudioFlows from '../pages/StudioFlows/StudioFlows';
import PhoneNumbers from '../pages/PhoneNumbers/PhoneNumbers';
import Conversations from '../pages/Conversations/Conversations';
import TaskRouter from '../pages/TaskRouter/TaskRouter';
import PrivateRoute from './PrivateRoute';
import Layout from '../components/Layout';

export default function AppRoutes() {
    return (
        <Routes>
            <Route path="/login" element={<Login />} />
            <Route element={<PrivateRoute />}>
                <Route element={<Layout />}>
                    <Route path="/" element={<Home />} />
                    <Route path="/studio" element={<StudioFlows />} />
                    <Route path="/phone-numbers" element={<PhoneNumbers />} />
                    <Route path="/conversations" element={<Conversations />} />
                    <Route path="/task-router" element={<TaskRouter />} />
                </Route>
            </Route>

            <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
    );
}
