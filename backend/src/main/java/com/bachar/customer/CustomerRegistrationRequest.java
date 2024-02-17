package com.bachar.customer;

import lombok.Builder;

@Builder
public record CustomerRegistrationRequest(String name, String email, Integer age) {
}
