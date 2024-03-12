package com.bachar.auth;

import com.bachar.customer.CustomerDTO;

public record AuthenticationResponse(String token, CustomerDTO customerDTO) {
}
