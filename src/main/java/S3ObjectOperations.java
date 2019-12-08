import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class S3ObjectOperations {
    public static void main(String[] args) throws IOException {
        long start, finish;
        double timeElapsed, throughput;

        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client.builder().region(region).build();
        String bucket = "bgn-misows";
        String key = "java-test.txt";

        List<String> lines = Arrays.asList("Sample text", "from Java");
        Path file = Paths.get(key);
        Files.write(file, lines, StandardCharsets.UTF_8);
        double bytes = file.toFile().length();
        System.out.println("File size: " + bytes);

        start = System.currentTimeMillis();
        s3.putObject(
                PutObjectRequest.builder().bucket(bucket).key(key).build(),
                file
        );
        finish = System.currentTimeMillis();
        timeElapsed = (finish - start) / 1000F;
        System.out.println("It took " + timeElapsed + " sec to upload that file.");
        throughput = (bytes / timeElapsed) * 8;
        System.out.println("Upload throughput: " + throughput + " bit/s");

        start = System.currentTimeMillis();
        s3.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build());
        finish = System.currentTimeMillis();
        timeElapsed = (finish - start) / 1000F;
        System.out.println("It took " + timeElapsed + " sec to download that file.");
        throughput = (bytes / timeElapsed) * 8;
        System.out.println("Download throughput: " + throughput + " bit/s");
    }
}
