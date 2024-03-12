package com.bachar.integrationTest;


import com.bachar.auth.AuthenticationRequest;
import com.bachar.auth.AuthenticationResponse;
import com.bachar.customer.CustomerDTO;
import com.bachar.customer.CustomerRegistrationRequest;
import com.bachar.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Random;

import static com.bachar.utils.GenerateRequests.generateRegistrationRequest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthIntegrationTest {

    public static final String REQUEST_LOGIN = "api/v1/auth/login";
    public static final String REQUEST_CUSTOMER = "api/v1/customers";
    private static final Random RANDOM = new Random();


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    void canLogin() {
        // create registration request
        CustomerRegistrationRequest request = generateRegistrationRequest();
        AuthenticationRequest requestLogin = new AuthenticationRequest(request.email(), request.password());

        //Login without being registered
        webTestClient.post()
                .uri(REQUEST_LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestLogin), AuthenticationRequest.class)
                .exchange().expectStatus().isUnauthorized();


        // Register customer
        webTestClient.post()
                .uri(REQUEST_CUSTOMER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //Login after being registered
        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(REQUEST_LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestLogin), AuthenticationRequest.class)
                .exchange().expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                }).returnResult();
        String jwt = result.getResponseHeaders().get("Authorization").get(0);
        CustomerDTO customerDTO = result.getResponseBody().customerDTO();
        assertThat(jwtUtil.isTokenValid(jwt, customerDTO.username())).isTrue();
        assertThat(customerDTO.email()).isEqualTo(request.email());
        assertThat(customerDTO.name()).isEqualTo(request.name());
        assertThat(customerDTO.age()).isEqualTo(request.age());
        assertThat(customerDTO.gender()).isEqualTo(request.gender());
        assertThat(customerDTO.username()).isEqualTo(request.email());
    }
}
