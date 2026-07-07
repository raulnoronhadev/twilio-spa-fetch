package twilio_spa_fetch_backend.ports;

import twilio_spa_fetch_backend.dto.BackupFileDTO;

import java.util.List;

public interface StoragePort {
    String uploadFile(byte[] fileData, String fileName, String contentType);
    byte[] downloadFile(String fileName);
    List<BackupFileDTO> listFiles(String prefix);
}
