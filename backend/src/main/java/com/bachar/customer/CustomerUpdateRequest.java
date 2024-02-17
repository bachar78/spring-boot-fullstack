package com.bachar.customer;

import lombok.Builder;

@Builder
public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
