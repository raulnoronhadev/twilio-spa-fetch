import { useEffect, useState } from 'react';
import { Alert, Box, Button, Chip, Stack, Typography } from '@mui/material';
import type { GridColDef, GridRenderCellParams } from '@mui/x-data-grid';
import DataTable from '../../components/DataTable';
import JsonDialog from '../../components/JsonDialog';
import RestoreBackupDialog from '../../components/RestoreBackupDialog';
import FeedbackSnackbar, { type Feedback } from '../../components/FeedbackSnackbar';
import {
    useFlows, useFlowDefinition, useBackupFlow, useBackupAllFlows, useRestoreFlow,
} from '../../hooks/useTwilioQueries';
import { useServerPagination } from '../../hooks/useServerPagination';
import type { FlowDTO } from '../../types/twilio';

export default function StudioFlows() {
    const [selectedFlowSid, setSelectedFlowSid] = useState<string | null>(null);
    const [restoreOpen, setRestoreOpen] = useState(false);
    const [feedback, setFeedback] = useState<Feedback | null>(null);

    const { paginationModel, pageToken, onPaginationModelChange, registerNextPageToken } = useServerPagination();
    const { data, isLoading, isError, error } = useFlows({ pageSize: paginationModel.pageSize, pageToken });
    useEffect(() => registerNextPageToken(data?.nextPageToken), [data?.nextPageToken, registerNextPageToken]);

    const definition = useFlowDefinition(selectedFlowSid);

    const backupFlow = useBackupFlow();
    const backupAll = useBackupAllFlows();
    const restoreFlow = useRestoreFlow();

    const handleBackup = (flowSid: string) => {
        backupFlow.mutate(flowSid, {
            onSuccess: (fileUrl) => setFeedback({ severity: 'success', message: `Backup created: ${fileUrl}` }),
            onError: (e) => setFeedback({ severity: 'error', message: `Backup failed: ${e.message}` }),
        });
    };

    const handleBackupAll = () => {
        backupAll.mutate(undefined, {
            onSuccess: (fileUrls) => setFeedback({ severity: 'success', message: `${fileUrls.length} flow(s) backed up.` }),
            onError: (e) => setFeedback({ severity: 'error', message: `Backup failed: ${e.message}` }),
        });
    };

    const handleRestore = (fileName: string) => {
        restoreFlow.mutate(fileName, {
            onSuccess: (res) => {
                setFeedback({ severity: 'success', message: `${res.message} — new flow SID: ${res.newFlowSid}` });
                setRestoreOpen(false);
            },
            onError: (e) => setFeedback({ severity: 'error', message: `Restore failed: ${e.message}` }),
        });
    };

    const columns: GridColDef<FlowDTO>[] = [
        { field: 'friendly_name', headerName: 'Name', flex: 1, minWidth: 180 },
        { field: 'sid', headerName: 'SID', flex: 1, minWidth: 280 },
        {
            field: 'status', headerName: 'Status', width: 130,
            renderCell: (params: GridRenderCellParams<FlowDTO>) => (
                <Chip
                    label={params.row.status}
                    color={params.row.status === 'published' ? 'success' : 'default'}
                    size="small"
                />
            ),
        },
        { field: 'revision', headerName: 'Revision', width: 100 },
        { field: 'date_updated', headerName: 'Updated', width: 200 },
        {
            field: 'actions', headerName: 'Actions', width: 260, sortable: false,
            renderCell: (params: GridRenderCellParams<FlowDTO>) => (
                <Stack direction="row" spacing={1} alignItems="center" height="100%">
                    <Button size="small" onClick={() => setSelectedFlowSid(params.row.sid)}>
                        Definition
                    </Button>
                    <Button
                        size="small"
                        variant="outlined"
                        disabled={backupFlow.isPending}
                        onClick={() => handleBackup(params.row.sid)}
                    >
                        Backup
                    </Button>
                </Stack>
            ),
        },
    ];

    return (
        <Box>
            <Stack direction="row" justifyContent="space-between" alignItems="center" mb={2}>
                <Typography variant="h5">Studio Flows</Typography>
                <Stack direction="row" spacing={1}>
                    <Button variant="outlined" onClick={() => setRestoreOpen(true)}>
                        Restore from backup
                    </Button>
                    <Button variant="contained" disabled={backupAll.isPending} onClick={handleBackupAll}>
                        {backupAll.isPending ? 'Backing up...' : 'Backup all flows'}
                    </Button>
                </Stack>
            </Stack>

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

            <JsonDialog
                open={selectedFlowSid !== null}
                title={`Flow definition — ${selectedFlowSid ?? ''}`}
                data={definition.isError ? { error: definition.error.message } : definition.data}
                loading={definition.isLoading}
                onClose={() => setSelectedFlowSid(null)}
            />

            <RestoreBackupDialog
                open={restoreOpen}
                title="Restore flow from backup"
                prefix="flows/"
                restoring={restoreFlow.isPending}
                onClose={() => setRestoreOpen(false)}
                onRestore={handleRestore}
            />

            <FeedbackSnackbar feedback={feedback} onClose={() => setFeedback(null)} />
        </Box>
    );
}
