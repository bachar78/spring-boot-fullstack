package com.bachar.customer;

import lombok.Builder;

@Builder
public record CustomerRegistrationRequest(String name, String email, String password, Integer age, Gender gender) {
}
