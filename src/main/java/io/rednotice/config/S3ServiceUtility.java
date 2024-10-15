package io.rednotice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3ServiceUtility {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(String dir, MultipartFile multipartFile) {
        try {
            // 파일명을 UUID로 생성
            String fileName = dir + UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

            // S3 업로드 요청 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(multipartFile.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            // 파일을 S3에 업로드 (InputStream을 사용하여 파일 데이터 전송)
            PutObjectResponse response = s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

            // 업로드된 파일의 URL 반환
            return "https://" + bucket + ".s3.amazonaws.com/" + fileName;

        } catch (S3Exception | IOException e) {
            throw new RuntimeException("파일 업로드 오류 발생", e);
        }
    }
}