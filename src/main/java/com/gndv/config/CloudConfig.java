package com.gndv.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudConfig {
    private final String endPoint = "https://kr.object.ncloudstorage.com";
    private final String regionName = "kr-standard";

    @Value("${spring.s3.accessKey}")
    private String accessKey;

    @Value("${spring.s3.secretKey}")
    private String secretKey;


    // S3 Client
    @Bean
    public AmazonS3 amazonS3Client(){
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    };
}
