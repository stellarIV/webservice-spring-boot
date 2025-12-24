// src/main/java/com/example/demo/StatusController.java
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
public class StatusController {

    @GetMapping("/status")
    public StatusResponse getStatus() {
        return new StatusResponse("Service Operational", LocalDateTime.now());
    }
}