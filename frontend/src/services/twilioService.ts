import { apiRequest } from './api';
import type {
    ActivityDTO,
    ConversationDTO,
    FlowDTO,
    PhoneDTO,
    RestoreResponse,
    TaskChannelDTO,
    TaskQueueDTO,
    WorkerDTO,
    WorkflowDTO,
    WorkspaceDTO,
} from '../types/twilio';

// --- Studio Flows ---

export const getAllFlows = () =>
    apiRequest<FlowDTO[]>('/Studio/Flows');

export const getFlowBySid = (flowSid: string) =>
    apiRequest<FlowDTO>(`/Studio/Flows/${flowSid}`);

export const getFlowDefinition = (flowSid: string) =>
    apiRequest<Record<string, unknown>>(`/Studio/Flows/${flowSid}/definition`);

export const backupFlow = (flowSid: string) =>
    apiRequest<string>(`/Studio/Flows/${flowSid}/backup`, { method: 'POST' });

export const backupAllFlows = () =>
    apiRequest<string[]>('/Studio/Flows/backup', { method: 'POST' });

export const restoreFlow = (fileName: string) =>
    apiRequest<RestoreResponse>('/Studio/Flows/restore', {
        method: 'POST',
        body: JSON.stringify({ fileName }),
    });

// --- Phone Numbers ---

export const getAllPhoneNumbers = () =>
    apiRequest<PhoneDTO[]>('/PhoneNumber/List');

// --- Conversations ---

export const getConversations = () =>
    apiRequest<ConversationDTO[]>('/Conversation/List');

// --- TaskRouter ---

export const getWorkspace = (workspaceSid: string) =>
    apiRequest<WorkspaceDTO>(`/TaskRouter/${workspaceSid}`);

export const getCompleteWorkspace = (workspaceSid: string) =>
    apiRequest<WorkspaceDTO>(`/TaskRouter/CompleteWorkspace/${workspaceSid}`);

export const getWorkers = (workspaceSid: string) =>
    apiRequest<WorkerDTO[]>(`/TaskRouter/${workspaceSid}/Workers`);

export const getWorkflows = (workspaceSid: string) =>
    apiRequest<WorkflowDTO[]>(`/TaskRouter/${workspaceSid}/Workflows`);

export const getTaskQueues = (workspaceSid: string) =>
    apiRequest<TaskQueueDTO[]>(`/TaskRouter/${workspaceSid}/TaskQueues`);

export const getTaskChannels = (workspaceSid: string) =>
    apiRequest<TaskChannelDTO[]>(`/TaskRouter/${workspaceSid}/TaskChannels`);

export const getActivities = (workspaceSid: string) =>
    apiRequest<ActivityDTO[]>(`/TaskRouter/${workspaceSid}/Activities`);

export const backupWorkspace = (workspaceSid: string) =>
    apiRequest<string>(`/TaskRouter/Workspace/backup/${workspaceSid}`, { method: 'POST' });

export const restoreWorkspace = (fileName: string) =>
    apiRequest<RestoreResponse>('/TaskRouter/Workspace/restore', {
        method: 'POST',
        body: JSON.stringify({ fileName }),
    });
