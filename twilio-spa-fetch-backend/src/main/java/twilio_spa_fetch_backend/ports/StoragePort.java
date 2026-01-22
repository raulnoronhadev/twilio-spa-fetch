package twilio_spa_fetch_backend.ports;

public interface StoragePort {
    String uploadFile(byte[] fileData, String fileName, String contentType);
}
