package twilio_spa_fetch_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twilio_spa_fetch_backend.dto.BackupFileDTO;
import twilio_spa_fetch_backend.ports.StoragePort;

import java.util.List;

@RestController
@RequestMapping("/api/v1/backups")
public class BackupController {

    @Autowired
    StoragePort storagePort;

    /**
     * Lists backup files stored in the bucket, newest first.
     * Use prefix "flows/" for Studio Flow backups or "workspace/" for TaskRouter workspaces.
     */
    @GetMapping
    public ResponseEntity<List<BackupFileDTO>> listBackups(@RequestParam(required = false, defaultValue = "") String prefix) {
        return ResponseEntity.ok(storagePort.listFiles(prefix));
    }
}
