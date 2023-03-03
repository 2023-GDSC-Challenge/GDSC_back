package com.solution.green.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GCPCode {
    SERVICE_ACCOUNT_JSON_PATH("C:/Users/12nov/바탕 화면/CodeAfter22F/green/src/main/resources/gcpBucketAccessKey.json"),
    // TODO - 이거 설정 안하면 com.google.api.client.googleapis.json.GoogleJsonResponseException: 401 Unauthorized POST https://storage.googleapis.com/upload/storage/ 에러 발생
    PROJECTED("gothic-imprint-378407"),
    ;
    private final String description;
}
