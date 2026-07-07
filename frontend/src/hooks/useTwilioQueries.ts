import { useMutation, useQuery } from '@tanstack/react-query';
import * as twilio from '../services/twilioService';

// --- Studio Flows ---

export function useFlows() {
    return useQuery({
        queryKey: ['flows'],
        queryFn: twilio.getAllFlows,
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

export function usePhoneNumbers() {
    return useQuery({
        queryKey: ['phoneNumbers'],
        queryFn: twilio.getAllPhoneNumbers,
    });
}

// --- Conversations ---

export function useConversations() {
    return useQuery({
        queryKey: ['conversations'],
        queryFn: twilio.getConversations,
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

export function useWorkers(workspaceSid: string | null) {
    return useQuery({
        queryKey: ['taskRouter', workspaceSid, 'workers'],
        queryFn: () => twilio.getWorkers(workspaceSid!),
        enabled: workspaceSid !== null,
    });
}

export function useWorkflows(workspaceSid: string | null) {
    return useQuery({
        queryKey: ['taskRouter', workspaceSid, 'workflows'],
        queryFn: () => twilio.getWorkflows(workspaceSid!),
        enabled: workspaceSid !== null,
    });
}

export function useTaskQueues(workspaceSid: string | null) {
    return useQuery({
        queryKey: ['taskRouter', workspaceSid, 'taskQueues'],
        queryFn: () => twilio.getTaskQueues(workspaceSid!),
        enabled: workspaceSid !== null,
    });
}

export function useTaskChannels(workspaceSid: string | null) {
    return useQuery({
        queryKey: ['taskRouter', workspaceSid, 'taskChannels'],
        queryFn: () => twilio.getTaskChannels(workspaceSid!),
        enabled: workspaceSid !== null,
    });
}

export function useActivities(workspaceSid: string | null) {
    return useQuery({
        queryKey: ['taskRouter', workspaceSid, 'activities'],
        queryFn: () => twilio.getActivities(workspaceSid!),
        enabled: workspaceSid !== null,
    });
}

export function useBackupWorkspace() {
    return useMutation({ mutationFn: twilio.backupWorkspace });
}

export function useRestoreWorkspace() {
    return useMutation({ mutationFn: twilio.restoreWorkspace });
}
