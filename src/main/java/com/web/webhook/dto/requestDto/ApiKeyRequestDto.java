package com.web.webhook.dto.requestDto;

public class ApiKeyRequestDto {

    private String keyName;

    private String status;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}