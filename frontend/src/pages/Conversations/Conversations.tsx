import { useEffect } from 'react';
import { Alert, Box, Chip, Typography } from '@mui/material';
import type { GridColDef, GridRenderCellParams } from '@mui/x-data-grid';
import DataTable from '../../components/DataTable';
import { useConversations } from '../../hooks/useTwilioQueries';
import { useServerPagination } from '../../hooks/useServerPagination';
import type { ConversationDTO } from '../../types/twilio';

const stateColor = (state: string): 'success' | 'default' | 'warning' => {
    if (state === 'active') return 'success';
    if (state === 'inactive') return 'warning';
    return 'default';
};

const columns: GridColDef<ConversationDTO>[] = [
    { field: 'friendly_name', headerName: 'Name', flex: 1, minWidth: 160 },
    { field: 'unique_name', headerName: 'Unique Name', flex: 1, minWidth: 160 },
    { field: 'sid', headerName: 'SID', flex: 1, minWidth: 280 },
    {
        field: 'state', headerName: 'State', width: 120,
        renderCell: (params: GridRenderCellParams<ConversationDTO>) => (
            <Chip label={params.row.state} color={stateColor(params.row.state)} size="small" />
        ),
    },
    { field: 'chat_service_sid', headerName: 'Chat Service SID', flex: 1, minWidth: 280 },
    { field: 'date_created', headerName: 'Created', width: 200 },
];

export default function Conversations() {
    const { paginationModel, pageToken, onPaginationModelChange, registerNextPageToken } = useServerPagination();
    const { data, isLoading, isError, error } = useConversations({ pageSize: paginationModel.pageSize, pageToken });
    useEffect(() => registerNextPageToken(data?.nextPageToken), [data?.nextPageToken, registerNextPageToken]);

    return (
        <Box>
            <Typography variant="h5" mb={2}>Conversations</Typography>
            {isError && <Alert severity="error" sx={{ mb: 2 }}>{error.message}</Alert>}
            <DataTable
                rows={data?.items ?? []}
                columns={columns}
                loading={isLoading}
                getRowId={(row) => row.sid}
                serverPagination={{
                    paginationModel,
                    onPaginationModelChange,
                    hasNextPage: Boolean(data?.nextPageToken),
                }}
            />
        </Box>
    );
}
