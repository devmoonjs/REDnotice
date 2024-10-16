package io.rednotice.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3ServiceUtil {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file, String fileUrl) {
        try {
            // 파일명을 UUID로 생성
            String fileName = fileUrl + UUID.randomUUID() + "_" + file.getOriginalFilename();

            // S3 업로드 요청 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            // 파일을 S3에 업로드 (InputStream을 사용하여 파일 데이터 전송)
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(),file.getSize()));

            // 업로드된 파일의 URL 반환
            return "https://" + bucket + ".s3.amazonaws.com/" + fileName;

        } catch (S3Exception e) {
            throw new RuntimeException("파일 업로드 오류 발생", e);
        } catch(IOException e){
            throw new RuntimeException("변환 오류 발생", e);
        }
    }

    public void deleteFile(String fileUrl, String bucketName) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

}