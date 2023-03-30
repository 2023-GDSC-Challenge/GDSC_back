package com.solution.green.service;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.solution.green.code.GCPCode.PROJECTED;
import static com.solution.green.code.GCPCode.SERVICE_ACCOUNT_JSON_PATH;

@Service
@RequiredArgsConstructor
public class GCSService {
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public String uploadImage(MultipartFile file) throws IOException {
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(
                        new ClassPathResource(
                                SERVICE_ACCOUNT_JSON_PATH.getDescription())
                                .getInputStream()))
                .setProjectId(PROJECTED.getDescription()).build().getService();
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
