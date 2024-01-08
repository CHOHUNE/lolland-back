package com.example.lollandback.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class AppConfig {
    @Value("${aws.accessKeyId}")
    private String accessKeyId;
    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        AwsCredentialsProvider provider = StaticCredentialsProvider.create(credentials);

        S3Client s3 = S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(provider)
                .build();

        return s3;
    }

    /*
    // 윈하는 위치에 복사해서 사용하세요

    private final S3Client s3;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    @Value("${image.file.prefix}")
    private String urlPrefix;

     */
}
