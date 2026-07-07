import { useEffect, useState, type FormEvent } from 'react';
import {
    Alert, Box, Button, Chip, Stack, Tab, Tabs, TextField, Typography,
} from '@mui/material';
import type { GridColDef, GridRenderCellParams, GridValidRowModel } from '@mui/x-data-grid';
import type { UseQueryResult } from '@tanstack/react-query';
import DataTable from '../../components/DataTable';
import JsonDialog from '../../components/JsonDialog';
import RestoreBackupDialog from '../../components/RestoreBackupDialog';
import FeedbackSnackbar, { type Feedback } from '../../components/FeedbackSnackbar';
import {
    useWorkspace, useWorkers, useWorkflows, useTaskQueues, useTaskChannels,
    useActivities, useBackupWorkspace, useRestoreWorkspace,
} from '../../hooks/useTwilioQueries';
import { useServerPagination } from '../../hooks/useServerPagination';
import type {
    ActivityDTO, Page, TaskChannelDTO, TaskQueueDTO, WorkerDTO, WorkflowDTO,
} from '../../types/twilio';

const availableChip = (available: boolean | null) => (
    <Chip
        label={available ? 'Available' : 'Unavailable'}
        color={available ? 'success' : 'default'}
        size="small"
    />
);

const workerColumns: GridColDef<WorkerDTO>[] = [
    { field: 'friendly_name', headerName: 'Name', flex: 1, minWidth: 160 },
    { field: 'sid', headerName: 'SID', flex: 1, minWidth: 280 },
    { field: 'activity_name', headerName: 'Activity', width: 140 },
    {
        field: 'available', headerName: 'Available', width: 130,
        renderCell: (params: GridRenderCellParams<WorkerDTO>) => availableChip(params.row.available),
    },
    { field: 'attributes', headerName: 'Attributes', flex: 1, minWidth: 200 },
];

const workflowColumns: GridColDef<WorkflowDTO>[] = [
    { field: 'friendly_name', headerName: 'Name', flex: 1, minWidth: 160 },
    { field: 'sid', headerName: 'SID', flex: 1, minWidth: 280 },
    { field: 'task_reservation_timeout', headerName: 'Reservation Timeout', width: 170 },
    { field: 'assignment_callback_url', headerName: 'Callback URL', flex: 1, minWidth: 200 },
    { field: 'date_updated', headerName: 'Updated', width: 200 },
];

const taskQueueColumns: GridColDef<TaskQueueDTO>[] = [
    { field: 'friendly_name', headerName: 'Name', flex: 1, minWidth: 160 },
    { field: 'sid', headerName: 'SID', flex: 1, minWidth: 280 },
    { field: 'target_workers', headerName: 'Target Workers', flex: 1, minWidth: 180 },
    { field: 'task_order', headerName: 'Task Order', width: 120 },
    { field: 'max_reserved_workers', headerName: 'Max Reserved', width: 130 },
    { field: 'assignment_activity_name', headerName: 'Assignment Activity', width: 170 },
];

const taskChannelColumns: GridColDef<TaskChannelDTO>[] = [
    { field: 'friendly_name', headerName: 'Name', flex: 1, minWidth: 160 },
    { field: 'unique_name', headerName: 'Unique Name', width: 160 },
    { field: 'sid', headerName: 'SID', flex: 1, minWidth: 280 },
    { field: 'channel_optimized_routing', headerName: 'Optimized Routing', width: 160 },
    { field: 'date_updated', headerName: 'Updated', width: 200 },
];

const activityColumns: GridColDef<ActivityDTO>[] = [
    { field: 'friendly_name', headerName: 'Name', flex: 1, minWidth: 160 },
    { field: 'sid', headerName: 'SID', flex: 1, minWidth: 280 },
    {
        field: 'available', headerName: 'Available', width: 130,
        renderCell: (params: GridRenderCellParams<ActivityDTO>) => availableChip(params.row.available),
    },
    { field: 'date_updated', headerName: 'Updated', width: 200 },
];

/** Presentational half of a paginated resource tab: table + error + cursor pagination. */
function ResourceTabView<T extends GridValidRowModel & { sid: string }>({ columns, query, pagination }: {
    columns: GridColDef<T>[];
    query: UseQueryResult<Page<T>, Error>;
    pagination: ReturnType<typeof useServerPagination>;
}) {
    const { data, isLoading, isError, error } = query;
    const { paginationModel, onPaginationModelChange, registerNextPageToken } = pagination;
    useEffect(() => registerNextPageToken(data?.nextPageToken), [data?.nextPageToken, registerNextPageToken]);

    return (
        <>
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
        </>
    );
}

// Each tab is its own component so it owns its query + pagination state and
// only the visible resource is fetched.

function WorkersTab({ workspaceSid }: { workspaceSid: string }) {
    const pagination = useServerPagination();
    const query = useWorkers(workspaceSid, { pageSize: pagination.paginationModel.pageSize, pageToken: pagination.pageToken });
    return <ResourceTabView columns={workerColumns} query={query} pagination={pagination} />;
}

function WorkflowsTab({ workspaceSid }: { workspaceSid: string }) {
    const pagination = useServerPagination();
    const query = useWorkflows(workspaceSid, { pageSize: pagination.paginationModel.pageSize, pageToken: pagination.pageToken });
    return <ResourceTabView columns={workflowColumns} query={query} pagination={pagination} />;
}

function TaskQueuesTab({ workspaceSid }: { workspaceSid: string }) {
    const pagination = useServerPagination();
    const query = useTaskQueues(workspaceSid, { pageSize: pagination.paginationModel.pageSize, pageToken: pagination.pageToken });
    return <ResourceTabView columns={taskQueueColumns} query={query} pagination={pagination} />;
}

function TaskChannelsTab({ workspaceSid }: { workspaceSid: string }) {
    const pagination = useServerPagination();
    const query = useTaskChannels(workspaceSid, { pageSize: pagination.paginationModel.pageSize, pageToken: pagination.pageToken });
    return <ResourceTabView columns={taskChannelColumns} query={query} pagination={pagination} />;
}

function ActivitiesTab({ workspaceSid }: { workspaceSid: string }) {
    const pagination = useServerPagination();
    const query = useActivities(workspaceSid, { pageSize: pagination.paginationModel.pageSize, pageToken: pagination.pageToken });
    return <ResourceTabView columns={activityColumns} query={query} pagination={pagination} />;
}

const TABS = ['Workers', 'Workflows', 'Task Queues', 'Task Channels', 'Activities'] as const;

export default function TaskRouter() {
    const [sidInput, setSidInput] = useState('');
    const [workspaceSid, setWorkspaceSid] = useState<string | null>(null);
    const [tab, setTab] = useState(0);
    const [detailsOpen, setDetailsOpen] = useState(false);
    const [restoreOpen, setRestoreOpen] = useState(false);
    const [feedback, setFeedback] = useState<Feedback | null>(null);

    const workspace = useWorkspace(workspaceSid);
    const backupWorkspace = useBackupWorkspace();
    const restoreWorkspace = useRestoreWorkspace();

    const handleLoad = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setWorkspaceSid(sidInput.trim());
    };

    const handleBackup = () => {
        if (workspaceSid === null) return;
        backupWorkspace.mutate(workspaceSid, {
            onSuccess: (fileUrl) => setFeedback({ severity: 'success', message: `Workspace backup created: ${fileUrl}` }),
            onError: (e) => setFeedback({ severity: 'error', message: `Backup failed: ${e.message}` }),
        });
    };

    const handleRestore = (fileName: string) => {
        restoreWorkspace.mutate(fileName, {
            onSuccess: (res) => {
                setFeedback({ severity: 'success', message: `${res.message} — new workspace SID: ${res.newWorkspaceSid}` });
                setRestoreOpen(false);
            },
            onError: (e) => setFeedback({ severity: 'error', message: `Restore failed: ${e.message}` }),
        });
    };

    return (
        <Box>
            <Stack direction="row" justifyContent="space-between" alignItems="center" mb={2}>
                <Typography variant="h5">TaskRouter</Typography>
                <Stack direction="row" spacing={1}>
                    <Button variant="outlined" onClick={() => setRestoreOpen(true)}>
                        Restore workspace
                    </Button>
                    <Button
                        variant="outlined"
                        disabled={workspaceSid === null || workspace.isLoading}
                        onClick={() => setDetailsOpen(true)}
                    >
                        Workspace details
                    </Button>
                    <Button
                        variant="contained"
                        disabled={workspaceSid === null || backupWorkspace.isPending}
                        onClick={handleBackup}
                    >
                        {backupWorkspace.isPending ? 'Backing up...' : 'Backup workspace'}
                    </Button>
                </Stack>
            </Stack>

            <form onSubmit={handleLoad}>
                <Stack direction="row" spacing={1} mb={2}>
                    <TextField
                        size="small"
                        label="Workspace SID"
                        placeholder="WSxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
                        value={sidInput}
                        onChange={(e) => setSidInput(e.target.value)}
                        sx={{ width: 400 }}
                    />
                    <Button type="submit" variant="contained" disabled={sidInput.trim() === ''}>
                        Load
                    </Button>
                </Stack>
            </form>

            {workspaceSid === null ? (
                <Alert severity="info">Enter a Workspace SID to explore its workers, workflows, queues, channels and activities.</Alert>
            ) : (
                <>
                    {workspace.data && (
                        <Typography variant="subtitle1" mb={1}>
                            Workspace: <strong>{workspace.data.friendly_name}</strong>
                        </Typography>
                    )}
                    <Tabs value={tab} onChange={(_, value: number) => setTab(value)} sx={{ mb: 2 }}>
                        {TABS.map((label) => <Tab key={label} label={label} />)}
                    </Tabs>
                    {tab === 0 && <WorkersTab key={workspaceSid} workspaceSid={workspaceSid} />}
                    {tab === 1 && <WorkflowsTab key={workspaceSid} workspaceSid={workspaceSid} />}
                    {tab === 2 && <TaskQueuesTab key={workspaceSid} workspaceSid={workspaceSid} />}
                    {tab === 3 && <TaskChannelsTab key={workspaceSid} workspaceSid={workspaceSid} />}
                    {tab === 4 && <ActivitiesTab key={workspaceSid} workspaceSid={workspaceSid} />}
                </>
            )}

            <JsonDialog
                open={detailsOpen}
                title={`Workspace — ${workspaceSid ?? ''}`}
                data={workspace.isError ? { error: workspace.error.message } : workspace.data}
                loading={workspace.isLoading}
                onClose={() => setDetailsOpen(false)}
            />

            <RestoreBackupDialog
                open={restoreOpen}
                title="Restore workspace from backup"
                prefix="workspace/"
                restoring={restoreWorkspace.isPending}
                onClose={() => setRestoreOpen(false)}
                onRestore={handleRestore}
            />

            <FeedbackSnackbar feedback={feedback} onClose={() => setFeedback(null)} />
        </Box>
    );
}
