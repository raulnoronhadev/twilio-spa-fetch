import { apiRequest } from './api';
import type {
    ActivityDTO,
    BackupFile,
    ConversationDTO,
    FlowDTO,
    Page,
    PageParams,
    PhoneDTO,
    RestoreFlowResponse,
    RestoreWorkspaceResponse,
    TaskChannelDTO,
    TaskQueueDTO,
    WorkerDTO,
    WorkflowDTO,
    WorkspaceDTO,
} from '../types/twilio';

function pageQuery({ pageSize, pageToken }: PageParams): string {
    const params = new URLSearchParams({ pageSize: String(pageSize) });
    if (pageToken) params.set('pageToken', pageToken);
    return params.toString();
}

// --- Studio Flows ---

export const getFlows = (params: PageParams) =>
    apiRequest<Page<FlowDTO>>(`/api/v1/studio/flows?${pageQuery(params)}`);

export const getFlowBySid = (flowSid: string) =>
    apiRequest<FlowDTO>(`/api/v1/studio/flows/${flowSid}`);

export const getFlowDefinition = (flowSid: string) =>
    apiRequest<Record<string, unknown>>(`/api/v1/studio/flows/${flowSid}/definition`);

export const backupFlow = (flowSid: string) =>
    apiRequest<string>(`/api/v1/studio/flows/${flowSid}/backup`, { method: 'POST' });

export const backupAllFlows = () =>
    apiRequest<string[]>('/api/v1/studio/flows/backup', { method: 'POST' });

export const restoreFlow = (fileName: string) =>
    apiRequest<RestoreFlowResponse>('/api/v1/studio/flows/restore', {
        method: 'POST',
        body: JSON.stringify({ fileName }),
    });

// --- Phone Numbers ---

export const getPhoneNumbers = (params: PageParams) =>
    apiRequest<Page<PhoneDTO>>(`/api/v1/phone-numbers?${pageQuery(params)}`);

// --- Conversations ---

export const getConversations = (params: PageParams) =>
    apiRequest<Page<ConversationDTO>>(`/api/v1/conversations?${pageQuery(params)}`);

// --- TaskRouter ---

const WORKSPACES = '/api/v1/task-router/workspaces';

export const getWorkspace = (workspaceSid: string) =>
    apiRequest<WorkspaceDTO>(`${WORKSPACES}/${workspaceSid}`);

export const getCompleteWorkspace = (workspaceSid: string) =>
    apiRequest<WorkspaceDTO>(`${WORKSPACES}/${workspaceSid}/complete`);

export const getWorkers = (workspaceSid: string, params: PageParams) =>
    apiRequest<Page<WorkerDTO>>(`${WORKSPACES}/${workspaceSid}/workers?${pageQuery(params)}`);

export const getWorkflows = (workspaceSid: string, params: PageParams) =>
    apiRequest<Page<WorkflowDTO>>(`${WORKSPACES}/${workspaceSid}/workflows?${pageQuery(params)}`);

export const getTaskQueues = (workspaceSid: string, params: PageParams) =>
    apiRequest<Page<TaskQueueDTO>>(`${WORKSPACES}/${workspaceSid}/task-queues?${pageQuery(params)}`);

export const getTaskChannels = (workspaceSid: string, params: PageParams) =>
    apiRequest<Page<TaskChannelDTO>>(`${WORKSPACES}/${workspaceSid}/task-channels?${pageQuery(params)}`);

export const getActivities = (workspaceSid: string, params: PageParams) =>
    apiRequest<Page<ActivityDTO>>(`${WORKSPACES}/${workspaceSid}/activities?${pageQuery(params)}`);

export const backupWorkspace = (workspaceSid: string) =>
    apiRequest<string>(`${WORKSPACES}/${workspaceSid}/backup`, { method: 'POST' });

export const restoreWorkspace = (fileName: string) =>
    apiRequest<RestoreWorkspaceResponse>(`${WORKSPACES}/restore`, {
        method: 'POST',
        body: JSON.stringify({ fileName }),
    });

// --- Backups ---

export const getBackups = (prefix: string) =>
    apiRequest<BackupFile[]>(`/api/v1/backups?${new URLSearchParams({ prefix })}`);
