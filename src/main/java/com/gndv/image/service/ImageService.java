package com.gndv.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final AmazonS3 amazonS3Client; // S3

    @Value("${spring.s3.bucket}")
    private String bucketName;

    public Object getImage(String image_type, Long image_id) {
        return amazonS3Client.getUrl(bucketName, image_type + "/" + image_id);
    }

    public String uploadCloud(String image_type, MultipartFile file) throws IOException {
        // 서버에 저장할 파일 이름을 생성 UUID
        String realFileName = UUID.randomUUID().toString();
        // 서버에 저장될 폴더명 = image_type

        // 파일 메타데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        // 저장될 위치 + 파일명
        String key = image_type + "/" + realFileName;
        
        // 클라우드에 파일 저장
        amazonS3Client.putObject(bucketName, key, file.getInputStream(), objectMetadata);
        amazonS3Client.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);

        return amazonS3Client.getUrl(bucketName, key).toString();
    }
}