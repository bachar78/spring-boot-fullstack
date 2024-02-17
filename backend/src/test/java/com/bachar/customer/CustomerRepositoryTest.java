package com.bachar.customer;

import com.bachar.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


//This disconnect from the original database
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// This annotation loads anything our jpa component need in order to run
@DataJpaTest
class CustomerRepositoryTest extends AbstractTestcontainers{

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
         underTest.deleteAll();
        //this is for reference
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);
        //When
        var existingCustomer = underTest.existsCustomerByEmail(customer.getEmail());
        //Then
        assertThat(existingCustomer).isTrue();
    }

    @Test
    void existsCustomerByEmailFailsWhenEmailDoesNotExist() {
        //Given
        var randomEmail = FAKER.internet().emailAddress();
        //When
        var existingCustomer = underTest.existsCustomerByEmail(randomEmail);
        //Then
        assertThat(existingCustomer).isFalse();
    }

    @Test
    void existsCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);
        Long customerId = underTest.findAll().stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        var existingCustomer = underTest.existsCustomerById(customerId);
        //Then
        assertThat(existingCustomer).isTrue();
    }

    @Test
    void existsCustomerByIdFailsWhenIdDoesNotExist() {
        //Given
        var id = -1L;
        //When
        var existingCustomer = underTest.existsCustomerById(id);
        //Then
        assertThat(existingCustomer).isFalse();
    }
}