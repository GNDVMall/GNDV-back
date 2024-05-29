package com.gndv.image.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final AmazonS3 amazonS3Client; // S3

    @Value("${spring.s3.bucket}")
    private String bucketName;

    public Object getImage(String image_type, Long image_id) {
        return amazonS3Client.getUrl(bucketName,image_type + "/" + image_id);
    }
}
