import { Box, Card, CardActionArea, CardContent, Stack, Typography } from '@mui/material';
import AccountTreeIcon from '@mui/icons-material/AccountTree';
import PhoneIcon from '@mui/icons-material/Phone';
import ForumIcon from '@mui/icons-material/Forum';
import HubIcon from '@mui/icons-material/Hub';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

const SECTIONS = [
    {
        title: 'Studio Flows',
        description: 'List flows, inspect definitions, backup and restore.',
        path: '/studio',
        icon: <AccountTreeIcon fontSize="large" color="primary" />,
    },
    {
        title: 'Phone Numbers',
        description: 'View the incoming phone numbers of your account.',
        path: '/phone-numbers',
        icon: <PhoneIcon fontSize="large" color="primary" />,
    },
    {
        title: 'Conversations',
        description: 'Browse the conversations of your account.',
        path: '/conversations',
        icon: <ForumIcon fontSize="large" color="primary" />,
    },
    {
        title: 'TaskRouter',
        description: 'Explore workspaces: workers, workflows, queues, channels and activities.',
        path: '/task-router',
        icon: <HubIcon fontSize="large" color="primary" />,
    },
];

export default function Home() {
    const navigate = useNavigate();
    const { accountName, accountSid } = useAuth();

    return (
        <Box>
            <Typography variant="h4" mb={1}>
                Welcome{accountName ? `, ${accountName}` : ''}
            </Typography>
            <Typography variant="body2" color="text.secondary" mb={4}>
                Account SID: {accountSid}
            </Typography>

            <Stack direction="row" flexWrap="wrap" gap={3}>
                {SECTIONS.map((section) => (
                    <Card key={section.path} sx={{ width: 280 }}>
                        <CardActionArea onClick={() => navigate(section.path)} sx={{ height: '100%' }}>
                            <CardContent>
                                {section.icon}
                                <Typography variant="h6" mt={1}>{section.title}</Typography>
                                <Typography variant="body2" color="text.secondary">
                                    {section.description}
                                </Typography>
                            </CardContent>
                        </CardActionArea>
                    </Card>
                ))}
            </Stack>
        </Box>
    );
}
