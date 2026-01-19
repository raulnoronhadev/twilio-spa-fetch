package twilio_spa_fetch_backend.service;
import com.twilio.base.ResourceSet;
import com.twilio.http.Response;
import com.twilio.rest.taskrouter.v1.Workspace;
import com.twilio.rest.taskrouter.v1.workspace.Worker;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import twilio_spa_fetch_backend.dto.PhoneNumberDTO;
import twilio_spa_fetch_backend.dto.TaskRouterBackupDTO;
import twilio_spa_fetch_backend.dto.WorkerRecordDTO;
import twilio_spa_fetch_backend.dto.WorkspaceRecordDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskRouterService {

    ResponseEntity<TaskRouterBackupDTO> taskRouterBackup(String workspaceSid) {
        ResponseEntity<WorkspaceRecordDTO> workspace = getWorkspaceBySid(workspaceSid);
        TaskRouterBackupDTO dto = new TaskRouterBackupDTO(
                workspace,

                )
    }

    public ResponseEntity<WorkspaceRecordDTO> getWorkspaceBySid(String workflowSid) {
        Workspace workspaceResource = Workspace.fetcher(workflowSid).fetch();
        WorkspaceRecordDTO dto = new WorkspaceRecordDTO(
                workspaceResource.getAccountSid(),
                workspaceResource.getSid(),
                workspaceResource.getFriendlyName(),
                workspaceResource.getDateCreated(),
                workspaceResource.getDateUpdated(),
                workspaceResource.getDefaultActivityName(),
                workspaceResource.getDefaultActivitySid()
        );
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<List<WorkerRecordDTO>> getAllWorkers(String workspaceSid) {
        ResourceSet<Worker> workers = Worker.reader(workspaceSid).limit(50).read();
        List<WorkerRecordDTO> dtoList = new ArrayList<>();
        for (Worker workerResource : workers) {
            WorkerRecordDTO dto = new WorkerRecordDTO(
            workerResource.getAccountSid(),
            workerResource.getFriendlyName(),
            workerResource.getSid(),
            workerResource.getWorkspaceSid(),
            workerResource.getActivityName(),
            workerResource.getAttributes(),
            workerResource.getAvailable(),
            workerResource.getDateCreated(),
            workerResource.getDateUpdated()
            );
            dtoList.add(dto);
        };
        return ResponseEntity.ok(dtoList);
    }

}
