package books.eda.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URL;
import java.time.Duration;

@Service
@ConditionalOnProperty(name = "app.s3.enabled", havingValue = "true", matchIfMissing = true)
public class CoverStorageService {

  private final S3Client s3;
  private final S3Presigner presigner;
  private final String bucket;
  private final String prefix;

  public CoverStorageService(
      @Value("${app.s3.bucket:books-eda-covers-dev}") String bucket,
      @Value("${app.s3.prefix:covers/}") String prefix,
      @Value("${app.dynamo.primaryRegion:us-east-1}") String region
  ) {
    Assert.hasText(bucket, "app.s3.bucket is required");
    Assert.hasText(region, "app.dynamo.primaryRegion is required");

    this.bucket = bucket;
    // normalize prefix to end with a single '/'
    this.prefix = (prefix == null || prefix.isBlank())
        ? ""
        : (prefix.endsWith("/") ? prefix : prefix + "/");

    try {
      var creds = DefaultCredentialsProvider.create();
      var reg = Region.of(region);

      this.s3 = S3Client.builder()
          .region(reg)
          .credentialsProvider(creds)
          .build();

      this.presigner = S3Presigner.builder()
          .region(reg)
          .credentialsProvider(creds)
          .build();

    } catch (Exception e) {
      throw new IllegalStateException(
          "Failed to initialize S3 client/presigner. " +
          "Check AWS credentials (AWS_PROFILE / env) and region.", e);
    }
  }

  /** Build a canonical key like covers/{bookId}/cover.png */
  public String keyFor(String bookId, String filename) {
    return (prefix + bookId + "/" + filename).replaceAll("/{2,}", "/");
  }

  /** Quick existence check for debugging */
  public boolean exists(String key) {
    try {
      s3.headObject(HeadObjectRequest.builder().bucket(bucket).key(key).build());
      return true;
    } catch (NoSuchKeyException e) {
      return false;
    }
  }

  /** Presign a PUT for the browser/uploader */
  public URL presignPut(String key, String contentType, Duration ttl) {
    var put = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType(contentType)
        .build();

    var presign = PutObjectPresignRequest.builder()
        .signatureDuration(ttl == null ? Duration.ofMinutes(10) : ttl)
        .putObjectRequest(put)
        .build();

    return presigner.presignPutObject(presign).url();
  }

  public String getBucket() { return bucket; }
  public String getPrefix() { return prefix; }
}
