package twilio_spa_fetch_backend.infrastructure;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ChecksumAlgorithm;
import twilio_spa_fetch_backend.ports.StoragePort;

import java.io.IOException;
import java.net.URI;

@Component
public class S3StorageAdapter implements StoragePort {

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
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .checksumValidationEnabled(false)
                        .build())
                .build();
    }

    @PostConstruct
    public void init() {
        try {
            s3Client.createBucket(builder -> builder.bucket(bucketName));
            System.out.println("Created bucket: " + bucketName);
        } catch (Exception e) {
            System.out.println("Bucket already exists or error while creating.: " + e.getMessage());
        }
    }

    @Override
    public String uploadFile(byte[] fileData, String fileName, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(fileName).contentType(contentType).checksumAlgorithm(ChecksumAlgorithm.UNKNOWN_TO_SDK_VERSION).build();
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileData));
        return String.format("%s/%s/%s", endpoint, bucketName, fileName);
    }

    @Override
    public byte[] downloadFile(String fileName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(fileName).build();
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);
            return response.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file: " + e.getMessage(), e);
        }
    }
}