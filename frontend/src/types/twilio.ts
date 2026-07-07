// Types mirroring the backend DTOs (JSON uses snake_case via @JsonProperty).

/** Cursor-based page returned by list endpoints. */
export interface Page<T> {
    items: T[];
    nextPageToken: string | null;
}

export interface PageParams {
    pageSize: number;
    pageToken?: string;
}

export interface BackupFile {
    fileName: string;
    size: number;
    lastModified: string;
}

export interface FlowDTO {
    account_sid: string;
    sid: string;
    friendly_name: string;
    date_created: string | null;
    date_updated: string | null;
    status: string;
    revision: number;
    commit_message: string | null;
    webhook_url: string | null;
    url: string | null;
    valid: boolean | null;
    errors: Record<string, unknown>[] | null;
    warnings: Record<string, unknown>[] | null;
    links: Record<string, string> | null;
    definition: Record<string, unknown> | null;
}

export interface PhoneDTO {
    account_sid: string;
    friendly_name: string;
    phone_number: string | null;
    phone_number_sid: string | null;
    identity_sid: string | null;
    date_created: string | null;
    date_updated: string | null;
    origin: string | null;
    status: string | null;
    smsUrl: string | null;
    voice_url: string | null;
}

export interface ConversationDTO {
    account_sid: string;
    chat_service_sid: string;
    messaging_service_sid: string | null;
    sid: string;
    friendly_name: string | null;
    unique_name: string | null;
    attributes: string | null;
    state: string;
    date_created: string | null;
    zone_date_time: string | null;
    timers: Record<string, unknown> | null;
    url: string | null;
    links: Record<string, string> | null;
    bindings: Record<string, unknown> | null;
}

export interface WorkerDTO {
    account_sid: string;
    friendly_name: string;
    sid: string;
    workspace_sid: string;
    activity_name: string | null;
    attributes: string | null;
    available: boolean | null;
    date_created: string | null;
    date_updated: string | null;
}

export interface WorkflowDTO {
    account_sid: string;
    sid: string;
    friendly_name: string;
    workspace_sid: string;
    assignment_callback_url: string | null;
    configuration: string | null;
    date_created: string | null;
    date_updated: string | null;
    task_reservation_timeout: number | null;
}

export interface TaskQueueDTO {
    account_sid: string;
    assignment_activity_name: string | null;
    assignment_activity_sid: string | null;
    date_created: string | null;
    date_updated: string | null;
    friendly_name: string;
    max_reserved_workers: number | null;
    links: Record<string, string> | null;
    reservation_activity_name: string | null;
    reservation_activity_sid: string | null;
    sid: string;
    target_workers: string | null;
    task_order: string | null;
    url: string | null;
    workspace_sid: string;
}

export interface TaskChannelDTO {
    account_sid: string;
    date_created: string | null;
    date_updated: string | null;
    friendly_name: string;
    sid: string;
    unique_name: string | null;
    url: string | null;
    workspace_sid: string;
    channel_optimized_routing: boolean | null;
    links: Record<string, string> | null;
}

export interface ActivityDTO {
    account_sid: string;
    available: boolean | null;
    date_created: string | null;
    date_updated: string | null;
    friendly_name: string;
    sid: string;
    url: string | null;
    workspace_sid: string;
    links: Record<string, string> | null;
}

export interface WorkspaceDTO {
    account_sid: string;
    sid: string;
    friendly_name: string;
    date_created: string | null;
    date_updated: string | null;
    default_activity_name: string | null;
    default_activity_sid: string | null;
    event_callback_url: string | null;
    events_filter: string | null;
    multi_task_enabled: boolean | null;
    timeout_activity_name: string | null;
    timeout_activity_sid: string | null;
    prioritize_queue_order: string | null;
    url: string | null;
    workflows: WorkflowDTO[] | null;
    workers: WorkerDTO[] | null;
    taskChannels: TaskChannelDTO[] | null;
    taskQueues: TaskQueueDTO[] | null;
    activities: ActivityDTO[] | null;
}

export interface RestoreFlowResponse {
    message: string;
    newFlowSid: string;
    restoredFrom: string;
}

export interface RestoreWorkspaceResponse {
    message: string;
    newWorkspaceSid: string;
    restoredFrom: string;
}
