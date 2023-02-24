package com.solution.green.service;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GCSService {
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    private String SERVICE_ACCOUNT_JSON_PATH = "C:/Users/12nov/바탕 화면/CodeAfter22F/green/src/main/resources/gcpBucketAccessKey.json";
    // TODO - 이거 설정 안하면 com.google.api.client.googleapis.json.GoogleJsonResponseException: 401 Unauthorized POST https://storage.googleapis.com/upload/storage/ 에러 발생
    private final String projectId = "gothic-imprint-378407";

    public String uploadImage(MultipartFile file) throws IOException {
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(
                        new FileInputStream(SERVICE_ACCOUNT_JSON_PATH)))
                .setProjectId(projectId).build().getService();
        String uuid = UUID.randomUUID().toString(); // GCS 에 저장될 파일 이름
        String ext = file.getContentType(); // 파일의 형식 ex) JPG

        // Cloud 에 이미지 업로드
        storage.create(BlobInfo.
                        newBuilder(bucketName, uuid).setContentType(ext).build(),
                file.getInputStream());

        // image url return
        return uuid;
    }
}
