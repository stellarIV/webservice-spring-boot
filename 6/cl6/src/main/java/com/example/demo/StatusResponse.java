// src/main/java/com/example/demo/StatusResponse.java
package com.example.demo;

import java.time.LocalDateTime;

public class StatusResponse {

    private final String status;
    private final LocalDateTime serverTime;

    public StatusResponse(String status, LocalDateTime serverTime) {
        this.status = status;
        this.serverTime = serverTime;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getServerTime() {
        return serverTime;
    }
}