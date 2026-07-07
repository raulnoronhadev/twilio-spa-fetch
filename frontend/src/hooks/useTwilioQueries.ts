import { keepPreviousData, useMutation, useQuery } from '@tanstack/react-query';
import * as twilio from '../services/twilioService';
import type { PageParams } from '../types/twilio';

// --- Studio Flows ---

export function useFlows(params: PageParams) {
    return useQuery({
        queryKey: ['flows', params],
        queryFn: () => twilio.getFlows(params),
        placeholderData: keepPreviousData,
    });
}

export function useFlowDefinition(flowSid: string | null) {
    return useQuery({
        queryKey: ['flows', flowSid, 'definition'],
        queryFn: () => twilio.getFlowDefinition(flowSid!),
        enabled: flowSid !== null,
    });
}

export function useBackupFlow() {
    return useMutation({ mutationFn: twilio.backupFlow });
}

export function useBackupAllFlows() {
    return useMutation({ mutationFn: twilio.backupAllFlows });
}

export function useRestoreFlow() {
    return useMutation({ mutationFn: twilio.restoreFlow });
}

// --- Phone Numbers ---

export function usePhoneNumbers(params: PageParams) {
    return useQuery({
        queryKey: ['phoneNumbers', params],
        queryFn: () => twilio.getPhoneNumbers(params),
        placeholderData: keepPreviousData,
    });
}

// --- Conversations ---

export function useConversations(params: PageParams) {
    return useQuery({
        queryKey: ['conversations', params],
        queryFn: () => twilio.getConversations(params),
        placeholderData: keepPreviousData,
    });
}

// --- TaskRouter ---

export function useWorkspace(workspaceSid: string | null) {
    return useQuery({
        queryKey: ['taskRouter', workspaceSid, 'workspace'],
        queryFn: () => twilio.getWorkspace(workspaceSid!),
        enabled: workspaceSid !== null,
    });
}

export function useWorkers(workspaceSid: string, params: PageParams) {
    return useQuery({
        queryKey: ['taskRouter', workspaceSid, 'workers', params],
        queryFn: () => twilio.getWorkers(workspaceSid, params),
        placeholderData: keepPreviousData,
    });
}

export function useWorkflows(workspaceSid: string, params: PageParams) {
    return useQuery({
        queryKey: ['taskRouter', workspaceSid, 'workflows', params],
        queryFn: () => twilio.getWorkflows(workspaceSid, params),
        placeholderData: keepPreviousData,
    });
}

export function useTaskQueues(workspaceSid: string, params: PageParams) {
    return useQuery({
        queryKey: ['taskRouter', workspaceSid, 'taskQueues', params],
        queryFn: () => twilio.getTaskQueues(workspaceSid, params),
        placeholderData: keepPreviousData,
    });
}

export function useTaskChannels(workspaceSid: string, params: PageParams) {
    return useQuery({
        queryKey: ['taskRouter', workspaceSid, 'taskChannels', params],
        queryFn: () => twilio.getTaskChannels(workspaceSid, params),
        placeholderData: keepPreviousData,
    });
}

export function useActivities(workspaceSid: string, params: PageParams) {
    return useQuery({
        queryKey: ['taskRouter', workspaceSid, 'activities', params],
        queryFn: () => twilio.getActivities(workspaceSid, params),
        placeholderData: keepPreviousData,
    });
}

export function useBackupWorkspace() {
    return useMutation({ mutationFn: twilio.backupWorkspace });
}

export function useRestoreWorkspace() {
    return useMutation({ mutationFn: twilio.restoreWorkspace });
}

// --- Backups ---

export function useBackups(prefix: string, enabled: boolean) {
    return useQuery({
        queryKey: ['backups', prefix],
        queryFn: () => twilio.getBackups(prefix),
        enabled,
    });
}
