package com.bachar.integrationTest;

import com.bachar.customer.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static com.bachar.utils.GenerateRequests.generateCustomerUpdateRequest;
import static com.bachar.utils.GenerateRequests.generateRegistrationRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


//this will fire up all the application and send http request

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {
    public static final String REQUEST_MAPPING = "api/v1/customers";
    @Autowired
    private CustomerRepository customerRepository;


    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    //WebTestClient gives us the ability to perform http requests as postman (Install the package)
    @Autowired
    private WebTestClient webTestClient;


    @Test
    void canRegisterCustomer() {
        // create registration request
        CustomerRegistrationRequest request = generateRegistrationRequest();

        // send a post request
        String jwt = Objects.requireNonNull(webTestClient.post()
                        .uri(REQUEST_MAPPING)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(request), CustomerRegistrationRequest.class)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .returnResult(Void.class)
                        .getResponseHeaders()
                        .get(AUTHORIZATION))
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get().uri(REQUEST_MAPPING)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwt))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // get customer by id
        var customerId = allCustomers.stream().filter(customer -> customer.email().equals(request.email()))
                .findFirst().map(CustomerDTO::id).orElseThrow();

        CustomerDTO expectedCustomerDTO = new CustomerDTO(customerId,
                request.name(),
                request.email(),
                request.age(),
                request.gender(),
                List.of("ROLE_USER"),
                request.email());
        assertThat(allCustomers).contains(expectedCustomerDTO);

        CustomerDTO expectedCustomerInDB = webTestClient.get().uri(REQUEST_MAPPING + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwt))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        assertThat(expectedCustomerInDB).isEqualTo(expectedCustomerDTO);
    }

    @Test
    void canDeleteCustomer() {
        // create customer 1
        CustomerRegistrationRequest request1 = generateRegistrationRequest();
        webTestClient.post()
                .uri(REQUEST_MAPPING)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request1), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();


        //Create customer2
        CustomerRegistrationRequest request2 = generateRegistrationRequest();
        // send a post request
        String jwtCustomer2 = Objects.requireNonNull(webTestClient.post()
                        .uri(REQUEST_MAPPING)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(request2), CustomerRegistrationRequest.class)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .returnResult(Void.class)
                        .getResponseHeaders()
                        .get(AUTHORIZATION))

                .get(0);

        // get all customers by customer 2
        List<CustomerDTO> allCustomer = webTestClient.get().uri(REQUEST_MAPPING)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtCustomer2))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // get customer1 id
        var customerId = allCustomer.stream().filter(customer -> customer.email().equals(request1.email()))
                .findFirst().map(CustomerDTO::id).orElseThrow();

        //Delete Customer1 by Customer2
        webTestClient.delete().uri(REQUEST_MAPPING + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtCustomer2))
                .exchange()
                .expectStatus()
                .isOk();
        webTestClient.get().uri(REQUEST_MAPPING + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtCustomer2))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // create registration request
        CustomerRegistrationRequest request1 = generateRegistrationRequest();

        // send a post request
        String jwt = Objects.requireNonNull(webTestClient.post()
                        .uri(REQUEST_MAPPING)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(request1), CustomerRegistrationRequest.class)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .returnResult(Void.class)
                        .getResponseHeaders()
                        .get(AUTHORIZATION))
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomer = webTestClient.get().uri(REQUEST_MAPPING)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwt))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // get customerId
        var customerId = allCustomer.stream().filter(customer -> customer.email().equals(request1.email()))
                .findFirst().map(CustomerDTO::id).orElseThrow();

        // Update the customer
        CustomerUpdateRequest updateRequest = generateCustomerUpdateRequest();
        String updatedJWT = Objects.requireNonNull(webTestClient.put()
                .uri(REQUEST_MAPPING + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)).get(0);

        //get Customer by id
        CustomerDTO updatedCustomerInDB = webTestClient.get().uri(REQUEST_MAPPING + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", updatedJWT))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();
        CustomerDTO expected = new CustomerDTO(customerId, updateRequest.name(), updateRequest.email(), updateRequest.age(), updateRequest.gender(), List.of("ROLE_USER"), updateRequest.email());
        assertThat(updatedCustomerInDB).isEqualTo(expected);
    }
}
