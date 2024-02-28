package com.bachar;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {

    record PingPong(String result) {
    }

    record HealthStatus(String status) {
    }

    @GetMapping("/ping")
    public PingPong getPingPong() {
        return new PingPong("Pong");
    }

    @GetMapping("/health")
    public HealthStatus getStatus() {
        return new HealthStatus("Up");
    }
}
