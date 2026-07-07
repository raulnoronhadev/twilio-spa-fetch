import {
    AppBar, Box, Drawer, List, ListItem, ListItemButton, ListItemIcon,
    ListItemText, Toolbar, Typography, Button, Divider,
} from '@mui/material';
import AccountTreeIcon from '@mui/icons-material/AccountTree';
import PhoneIcon from '@mui/icons-material/Phone';
import ForumIcon from '@mui/icons-material/Forum';
import HubIcon from '@mui/icons-material/Hub';
import HomeIcon from '@mui/icons-material/Home';
import LogoutIcon from '@mui/icons-material/Logout';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import { useAuth, useLogout } from '../hooks/useAuth';

const DRAWER_WIDTH = 240;

const NAV_ITEMS = [
    { label: 'Home', path: '/', icon: <HomeIcon /> },
    { label: 'Studio Flows', path: '/studio', icon: <AccountTreeIcon /> },
    { label: 'Phone Numbers', path: '/phone-numbers', icon: <PhoneIcon /> },
    { label: 'Conversations', path: '/conversations', icon: <ForumIcon /> },
    { label: 'TaskRouter', path: '/task-router', icon: <HubIcon /> },
];

export default function Layout() {
    const navigate = useNavigate();
    const location = useLocation();
    const { accountName, accountSid } = useAuth();
    const { mutate: logout, isPending } = useLogout();

    return (
        <Box display="flex" minHeight="100vh" width="100%" bgcolor="#f5f5f5">
            <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
                <Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Typography variant="h6" noWrap>
                        Twilio SPA Fetch
                    </Typography>
                    <Box display="flex" alignItems="center" gap={2}>
                        <Typography variant="body2">
                            {accountName ?? accountSid}
                        </Typography>
                        <Button
                            color="inherit"
                            startIcon={<LogoutIcon />}
                            onClick={() => logout()}
                            disabled={isPending}
                        >
                            Logout
                        </Button>
                    </Box>
                </Toolbar>
            </AppBar>

            <Drawer
                variant="permanent"
                sx={{
                    width: DRAWER_WIDTH,
                    flexShrink: 0,
                    '& .MuiDrawer-paper': { width: DRAWER_WIDTH, boxSizing: 'border-box' },
                }}
            >
                <Toolbar />
                <Divider />
                <List>
                    {NAV_ITEMS.map((item) => (
                        <ListItem key={item.path} disablePadding>
                            <ListItemButton
                                selected={location.pathname === item.path}
                                onClick={() => navigate(item.path)}
                            >
                                <ListItemIcon>{item.icon}</ListItemIcon>
                                <ListItemText primary={item.label} />
                            </ListItemButton>
                        </ListItem>
                    ))}
                </List>
            </Drawer>

            <Box component="main" flexGrow={1} p={3} sx={{ overflow: 'auto' }}>
                <Toolbar />
                <Outlet />
            </Box>
        </Box>
    );
}
