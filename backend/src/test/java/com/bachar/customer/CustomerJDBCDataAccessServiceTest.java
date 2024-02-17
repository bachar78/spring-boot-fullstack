package com.bachar.customer;

import com.bachar.AbstractTestcontainers;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();
    private CustomerJDBCDataAccessService underTest;


    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(getJdbcTemplate(), customerRowMapper);
    }

    @Test
    void getAllCustomers() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        //When
        List<Customer> allCustomers = underTest.getAllCustomers();

        //Then
        assertThat(allCustomers).isNotEmpty();
    }

    @Test
    void getCustomerById() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Long customerId = underTest.getAllCustomers().stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        Optional<Customer> actualCustomer = underTest.getCustomerById(customerId);
        //Then
        assertThat(actualCustomer).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(customerId);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void getCustomerByWrongIdError() {
        //Given
        Long id = -1L;

        //When
        var expectedCustomer = underTest.getCustomerById(id);

        //then
        assertThat(expectedCustomer).isEmpty();

    }

    @Test
    void insertCustomer() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        //when
        underTest.insertCustomer(customer);
        Optional<Customer> customerInDB = underTest.getAllCustomers().stream().filter(c -> c.getEmail().equals(email))
                .findFirst();
        AssertionsForClassTypes.assertThat(customerInDB).isPresent().hasValueSatisfying(c -> {
            AssertionsForClassTypes.assertThat(c.getName()).isEqualTo(customer.getName());
            AssertionsForClassTypes.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            AssertionsForClassTypes.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void checkIfEmailExist() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        //When
        boolean response = underTest.checkIfEmailExist(customer.getEmail());
        //Then
        assertThat(response).isTrue();
    }

    @Test
    void checkCustomerById() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        Long customerId = underTest.getAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst()
                .map(Customer::getId).orElseThrow();
        //When
        boolean response = underTest.checkCustomerById(customerId);

        //Then
        assertThat(response).isTrue();
    }

    @Test
    void deleteCustomer() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        Long customerId = underTest.getAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst()
                .map(Customer::getId).orElseThrow();
        //When
        underTest.deleteCustomer(customerId);
        //Then
        assertThat(underTest.getCustomerById(customerId)).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        Customer customerInDB = underTest.getAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst()
                .orElseThrow();
        //When
        var newName = "Bachar";
        customerInDB.setName(newName);
        underTest.updateCustomer(customerInDB);
        //Then
        Optional<Customer> updatedCustomer = underTest.getCustomerById(customerInDB.getId());
        AssertionsForClassTypes.assertThat(updatedCustomer).isPresent().hasValueSatisfying(c -> {
            AssertionsForClassTypes.assertThat(c.getId()).isEqualTo(customerInDB.getId());
            AssertionsForClassTypes.assertThat(c.getName()).isEqualTo(newName);
            AssertionsForClassTypes.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            AssertionsForClassTypes.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
    @Test
    void updateCustomerEmail() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        Customer customerInDB = underTest.getAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst()
                .orElseThrow();
        //When
        var newEmail = "bachar@test.com";
        customerInDB.setEmail(newEmail);
        underTest.updateCustomer(customerInDB);
        //Then
        Optional<Customer> updatedInDB = underTest.getCustomerById(customerInDB.getId());
        AssertionsForClassTypes.assertThat(updatedInDB).isPresent().hasValueSatisfying(c -> {
            AssertionsForClassTypes.assertThat(c.getId()).isEqualTo(customerInDB.getId());
            AssertionsForClassTypes.assertThat(c.getName()).isEqualTo(customer.getName());
            AssertionsForClassTypes.assertThat(c.getEmail()).isEqualTo(newEmail);
            AssertionsForClassTypes.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        Customer customerInDB = underTest.getAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst()
                .orElseThrow();
        //When
        var newAge = 12;
        customerInDB.setAge(newAge);
        underTest.updateCustomer(customerInDB);
        //Then
        Optional<Customer> updatedInDB = underTest.getCustomerById(customerInDB.getId());
        AssertionsForClassTypes.assertThat(updatedInDB).isPresent().hasValueSatisfying(c -> {
            AssertionsForClassTypes.assertThat(c.getId()).isEqualTo(customerInDB.getId());
            AssertionsForClassTypes.assertThat(c.getName()).isEqualTo(customer.getName());
            AssertionsForClassTypes.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            AssertionsForClassTypes.assertThat(c.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void updateAllCustomerProperties() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        Customer customerInDB = underTest.getAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .findFirst()
                .orElseThrow();
        //When
        var newName = "Samer";
        var newEmail = "samer@example.com";
        var newAge = 42;

        customerInDB.setName(newName);
        customerInDB.setEmail(newEmail);
        customerInDB.setAge(newAge);
        underTest.updateCustomer(customerInDB);
        //Then
        Optional<Customer> updatedInDB = underTest.getCustomerById(customerInDB.getId());
        AssertionsForClassTypes.assertThat(updatedInDB).isPresent().hasValueSatisfying(c -> {
            AssertionsForClassTypes.assertThat(c.getId()).isEqualTo(customerInDB.getId());
            AssertionsForClassTypes.assertThat(c.getName()).isEqualTo(newName);
            AssertionsForClassTypes.assertThat(c.getEmail()).isEqualTo(newEmail);
            AssertionsForClassTypes.assertThat(c.getAge()).isEqualTo(newAge);
        });
    }

}