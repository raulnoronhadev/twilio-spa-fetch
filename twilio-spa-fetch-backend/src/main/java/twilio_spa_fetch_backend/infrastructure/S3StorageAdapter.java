package twilio_spa_fetch_backend.infrastructure;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.checksums.RequestChecksumCalculation;
import software.amazon.awssdk.core.checksums.ResponseChecksumValidation;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import twilio_spa_fetch_backend.dto.BackupFileDTO;
import twilio_spa_fetch_backend.exception.StorageException;
import twilio_spa_fetch_backend.ports.StoragePort;

import java.io.IOException;
import java.net.URI;
import java.util.Comparator;
import java.util.List;

@Component
public class S3StorageAdapter implements StoragePort {

    private static final Logger log = LoggerFactory.getLogger(S3StorageAdapter.class);

    private final S3Client s3Client;
    private final String bucketName;
    private final String endpoint;

    public S3StorageAdapter(@Value("${aws.s3.region}") String region,
                            @Value("${aws.s3.bucket-name}") String bucketName,
                            @Value("${aws.s3.endpoint}") String endpoint,
                            @Value("${aws.s3.access-key}") String accessKey,
                            @Value("${aws.s3.secret-key}") String secretKey) {
        this.bucketName = bucketName;
        this.endpoint = endpoint;

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .endpointOverride(URI.create(endpoint))
                // S3-compatible servers (Alarik) don't support the aws-chunked
                // streaming checksums the SDK sends by default since v2.30.
                .requestChecksumCalculation(RequestChecksumCalculation.WHEN_REQUIRED)
                .responseChecksumValidation(ResponseChecksumValidation.WHEN_REQUIRED)
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

    @PostConstruct
    public void init() {
        try {
            s3Client.createBucket(builder -> builder.bucket(bucketName));
            log.info("Created bucket: {}", bucketName);
        } catch (Exception e) {
            log.warn("Bucket already exists or error while creating: {}", e.getMessage());
        }
    }

    @Override
    public String uploadFile(byte[] fileData, String fileName, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(fileName).contentType(contentType).build();
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileData));
        return String.format("%s/%s/%s", endpoint, bucketName, fileName);
    }

    @Override
    public List<BackupFileDTO> listFiles(String prefix) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();
        return s3Client.listObjectsV2Paginator(request).contents().stream()
                .map(object -> new BackupFileDTO(object.key(), object.size(), object.lastModified()))
                .sorted(Comparator.comparing(BackupFileDTO::lastModified).reversed())
                .toList();
    }

    @Override
    public byte[] downloadFile(String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(fileName).build();
        try (ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest)) {
            return response.readAllBytes();
        } catch (IOException e) {
            throw new StorageException("Error reading backup file: " + fileName, e);
        }
    }
}