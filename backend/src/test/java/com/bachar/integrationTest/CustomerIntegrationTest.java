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

import static com.bachar.utils.GenerateRequests.generateCustomerUpdateRequest;
import static com.bachar.utils.GenerateRequests.generateRegistrationRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


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
        webTestClient.post()
                .uri(REQUEST_MAPPING)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        // get all customers
        List<Customer> allCustomer = webTestClient.get().uri(REQUEST_MAPPING)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure that customer is present
        Customer expectedCustomer = new Customer(request.name(), request.email(), request.age());
        assertThat(allCustomer)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        // get customer by id
        var customerId = allCustomer.stream().filter(customer -> customer.getEmail().equals(request.email()))
                .findFirst().map(Customer::getId).orElseThrow();

        Customer expectedCustomerInDB = webTestClient.get().uri(REQUEST_MAPPING + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();
        expectedCustomer.setId(customerId);
        assertThat(expectedCustomerInDB).isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {
        // create registration request
        CustomerRegistrationRequest request = generateRegistrationRequest();

        // send a post request
        webTestClient.post()
                .uri(REQUEST_MAPPING)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        // get all customers
        List<Customer> allCustomer = webTestClient.get().uri(REQUEST_MAPPING)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // get customer by id
        var customerId = allCustomer.stream().filter(customer -> customer.getEmail().equals(request.email()))
                .findFirst().map(Customer::getId).orElseThrow();

        //Delete Customer
        webTestClient.delete().uri(REQUEST_MAPPING + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
        webTestClient.get().uri(REQUEST_MAPPING + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // create registration request
        CustomerRegistrationRequest request = generateRegistrationRequest();

        // send a post request
        webTestClient.post()
                .uri(REQUEST_MAPPING)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        // get all customers
        List<Customer> allCustomer = webTestClient.get().uri(REQUEST_MAPPING)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // get customerId
        var customerId = allCustomer.stream().filter(customer -> customer.getEmail().equals(request.email()))
                .findFirst().map(Customer::getId).orElseThrow();

        // Update the customer
        CustomerUpdateRequest updateRequest = generateCustomerUpdateRequest();
        webTestClient.put()
                .uri(REQUEST_MAPPING + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus().isOk();
        //get Customer by id
        Customer updatedCustomerInDB = webTestClient.get().uri(REQUEST_MAPPING + "/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();
        Customer expected = Customer.builder().id(customerId).name(updateRequest.name())
                .email(updateRequest.email())
                .age(updateRequest.age()).build();
         assertThat(updatedCustomerInDB).isEqualTo(expected);
    }
}
