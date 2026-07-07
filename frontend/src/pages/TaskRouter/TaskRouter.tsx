import { useState, type FormEvent } from 'react';
import {
    Alert, Box, Button, Chip, Dialog, DialogActions, DialogContent,
    DialogTitle, Stack, Tab, Tabs, TextField, Typography,
} from '@mui/material';
import type { GridColDef, GridRenderCellParams } from '@mui/x-data-grid';
import DataTable from '../../components/DataTable';
import JsonDialog from '../../components/JsonDialog';
import FeedbackSnackbar, { type Feedback } from '../../components/FeedbackSnackbar';
import {
    useWorkspace, useWorkers, useWorkflows, useTaskQueues, useTaskChannels,
    useActivities, useBackupWorkspace, useRestoreWorkspace,
} from '../../hooks/useTwilioQueries';
import type {
    ActivityDTO, TaskChannelDTO, TaskQueueDTO, WorkerDTO, WorkflowDTO,
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

const TABS = ['Workers', 'Workflows', 'Task Queues', 'Task Channels', 'Activities'] as const;

export default function TaskRouter() {
    const [sidInput, setSidInput] = useState('');
    const [workspaceSid, setWorkspaceSid] = useState<string | null>(null);
    const [tab, setTab] = useState(0);
    const [detailsOpen, setDetailsOpen] = useState(false);
    const [restoreOpen, setRestoreOpen] = useState(false);
    const [fileName, setFileName] = useState('');
    const [feedback, setFeedback] = useState<Feedback | null>(null);

    const workspace = useWorkspace(workspaceSid);
    const workers = useWorkers(workspaceSid);
    const workflows = useWorkflows(workspaceSid);
    const taskQueues = useTaskQueues(workspaceSid);
    const taskChannels = useTaskChannels(workspaceSid);
    const activities = useActivities(workspaceSid);

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

    const handleRestore = () => {
        restoreWorkspace.mutate(fileName, {
            onSuccess: (res) => {
                setFeedback({ severity: 'success', message: `${res.message} — new workspace SID: ${res.newFlowSid}` });
                setRestoreOpen(false);
                setFileName('');
            },
            onError: (e) => setFeedback({ severity: 'error', message: `Restore failed: ${e.message}` }),
        });
    };

    const tables = [
        <DataTable key="workers" rows={workers.data ?? []} columns={workerColumns} loading={workers.isLoading} getRowId={(r) => r.sid} />,
        <DataTable key="workflows" rows={workflows.data ?? []} columns={workflowColumns} loading={workflows.isLoading} getRowId={(r) => r.sid} />,
        <DataTable key="taskQueues" rows={taskQueues.data ?? []} columns={taskQueueColumns} loading={taskQueues.isLoading} getRowId={(r) => r.sid} />,
        <DataTable key="taskChannels" rows={taskChannels.data ?? []} columns={taskChannelColumns} loading={taskChannels.isLoading} getRowId={(r) => r.sid} />,
        <DataTable key="activities" rows={activities.data ?? []} columns={activityColumns} loading={activities.isLoading} getRowId={(r) => r.sid} />,
    ];

    const queries = [workers, workflows, taskQueues, taskChannels, activities];
    const activeQuery = queries[tab];

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
                    {activeQuery.isError && (
                        <Alert severity="error" sx={{ mb: 2 }}>{activeQuery.error.message}</Alert>
                    )}
                    {tables[tab]}
                </>
            )}

            <JsonDialog
                open={detailsOpen}
                title={`Workspace — ${workspaceSid ?? ''}`}
                data={workspace.isError ? { error: workspace.error.message } : workspace.data}
                loading={workspace.isLoading}
                onClose={() => setDetailsOpen(false)}
            />

            <Dialog open={restoreOpen} onClose={() => setRestoreOpen(false)} fullWidth>
                <DialogTitle>Restore workspace from backup</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        fullWidth
                        margin="dense"
                        label="Backup file name"
                        value={fileName}
                        onChange={(e) => setFileName(e.target.value)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setRestoreOpen(false)}>Cancel</Button>
                    <Button
                        variant="contained"
                        disabled={fileName.trim() === '' || restoreWorkspace.isPending}
                        onClick={handleRestore}
                    >
                        {restoreWorkspace.isPending ? 'Restoring...' : 'Restore'}
                    </Button>
                </DialogActions>
            </Dialog>

            <FeedbackSnackbar feedback={feedback} onClose={() => setFeedback(null)} />
        </Box>
    );
}
