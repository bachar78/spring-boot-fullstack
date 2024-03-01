package com.bachar;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    private static int COUNTER = 0;

    record HealthStatus(String status) {
    }

    @GetMapping("/health")
    public HealthStatus getStatus() {
        return new HealthStatus("Up url has been called: %s times".formatted(++COUNTER) );
    }
}
