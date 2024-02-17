package com.bachar.customer;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJPADateAccessServiceTest {
    Faker faker = new Faker();
    private CustomerJPADateAccessService underTest;

    @Mock
    private CustomerRepository customerRepository;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADateAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();
        //Then
        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById() {
        //Given
        var id = 1L;
        //When
        underTest.getCustomerById(id);
        //Then
        Mockito.verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {

        //Given
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );

        //When
        underTest.insertCustomer(customer);
        //Then
        customerRepository.save(customer);
    }

    @Test
    void checkIfEmailExist() {
        //Given
        var email = faker.internet().safeEmailAddress();
        //When
        underTest.checkIfEmailExist(email);
        //Then
        customerRepository.existsCustomerByEmail(email);
    }

    @Test
    void checkCustomerById() {
        //Given
        var id = 1L;
        //When
        underTest.checkCustomerById(id);
        //Then
        customerRepository.existsCustomerById(id);
    }

    @Test
    void deleteCustomer() {
        //Given
        var id = 1L;
        //When
        underTest.deleteCustomer(id);
        //Then
        customerRepository.deleteById(id);
    }

    @Test
    void updateCustomer() {
        //Given
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        //When
        underTest.updateCustomer(customer);
        //Then
        customerRepository.save(customer);
    }
}