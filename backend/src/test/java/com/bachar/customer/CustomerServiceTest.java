package com.bachar.customer;

import com.bachar.exception.DuplicateResourceException;
import com.bachar.exception.RequestValidationException;
import com.bachar.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    Customer customer = new Customer();
    private CustomerService underTest;

    @Mock
    private CustomerDao customerDao;


    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void selectAllCustomers() {
        //When
        underTest.selectAllCustomers();
        //Then
        verify(customerDao).getAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //Given
        var id = 1L;
        Customer customer = new Customer(id, "Bachar", "bachar@example.com", 23);
        //When
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));
        //Then
        Customer customerInDB = underTest.getCustomer(id);
        assertThat(customerInDB).isEqualTo(customer);
    }

    @Test
    void willThroughWhenGetCustomerReturnsEmptyOptional() {
        //Given
        var id = 1L;

        //When
        when(customerDao.getCustomerById(id)).thenReturn(Optional.empty());
        //Then
        assertThatThrownBy(() -> underTest.getCustomer(id)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] is not found".formatted(id));
    }

    @Test
    void addCustomer() {
        //Given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Bachar", "bachar@example.com", 45);
        //When
        when(customerDao.checkIfEmailExist(request.email())).thenReturn(false);
        //Then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        underTest.addCustomer(request);
        verify(customerDao).insertCustomer(argumentCaptor.capture());
        Customer customerCaptured = argumentCaptor.getValue();
        assertThat(customerCaptured.getId()).isNull();
        assertThat(customerCaptured.getName()).isEqualTo(request.name());
        assertThat(customerCaptured.getEmail()).isEqualTo(request.email());
        assertThat(customerCaptured.getAge()).isEqualTo(request.age());
    }

    @Test
    void addCustomerThrowError() {
        //Given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Bachar", "bachar@example.com", 45);
        //When
        when(customerDao.checkIfEmailExist(request.email())).thenReturn(true);
        //Then
        assertThatThrownBy(() -> underTest.addCustomer(request)).isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");
    }

    @Test
    void removeCustomer() {
        //Given
        var id = 1L;
        //When
        when(customerDao.checkCustomerById(id)).thenReturn(true);
        underTest.removeCustomer(id);
        //Then
        verify(customerDao).deleteCustomer(id);
    }

    @Test
    void removeCustomerThrowError() {
        //Given
        var id = 1L;
        //When
        when(customerDao.checkCustomerById(id)).thenReturn(false);
        //Then
        assertThatThrownBy(() -> underTest.removeCustomer(id)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] is not found".formatted(id));
        verify(customerDao, never()).deleteCustomer(id);
    }

    @Test
    void updateAllCustomerProperties() {
        //Given
        var id = 1L;
        Customer customer = new Customer(id, "Bachar", "bachar@example.com", 23);
        //When
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        CustomerUpdateRequest request = new CustomerUpdateRequest("Samer", "samer@example.com", 43);
        when(customerDao.checkIfEmailExist(request.email())).thenReturn(false);
        underTest.updateCustomer(request, id);
        //Then
        ArgumentCaptor<Customer> captorCustomer = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(captorCustomer.capture());
        Customer customerUpdated = captorCustomer.getValue();
        assertThat(customerUpdated.getName()).isEqualTo(request.name());
        assertThat(customerUpdated.getEmail()).isEqualTo(request.email());
        assertThat(customerUpdated.getAge()).isEqualTo(request.age());
    }

    @Test
    void updateCustomerName() {
        //Given
        var id = 1L;
        Customer customer = new Customer(id, "Bachar", "bachar@example.com", 23);
        //When
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        CustomerUpdateRequest request = new CustomerUpdateRequest("Samer", null, null);

        underTest.updateCustomer(request, id);
        //Then
        ArgumentCaptor<Customer> captorCustomer = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(captorCustomer.capture());
        Customer customerUpdated = captorCustomer.getValue();
        assertThat(customerUpdated.getName()).isEqualTo(request.name());
        assertThat(customerUpdated.getEmail()).isEqualTo(customer.getEmail());
        assertThat(customerUpdated.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateCustomerEmail() {
        //Given
        var id = 1L;
        Customer customer = new Customer(id, "Bachar", "bachar@example.com", 23);
        //When
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        CustomerUpdateRequest request = new CustomerUpdateRequest(null, "samer@example.com", null);
        when(customerDao.checkIfEmailExist(request.email())).thenReturn(false);
        underTest.updateCustomer(request, id);
        //Then
        ArgumentCaptor<Customer> captorCustomer = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(captorCustomer.capture());
        Customer customerUpdated = captorCustomer.getValue();
        assertThat(customerUpdated.getName()).isEqualTo(customer.getName());
        assertThat(customerUpdated.getEmail()).isEqualTo(request.email());
        assertThat(customerUpdated.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateCustomerAge() {
        //Given
        var id = 1L;
        Customer customer = new Customer(id, "Bachar", "bachar@example.com", 23);
        //When
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        CustomerUpdateRequest request = new CustomerUpdateRequest(null, null, 33);
        underTest.updateCustomer(request, id);
        //Then
        ArgumentCaptor<Customer> captorCustomer = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(captorCustomer.capture());
        Customer customerUpdated = captorCustomer.getValue();
        assertThat(customerUpdated.getName()).isEqualTo(customer.getName());
        assertThat(customerUpdated.getEmail()).isEqualTo(customer.getEmail());
        assertThat(customerUpdated.getAge()).isEqualTo(request.age());
    }

    @Test
    void updateCustomerFailsUpdatedEmailExist() {
        //Given
        var id = 1L;
        Customer customer = new Customer(id, "Bachar", "bachar@example.com", 23);
        //When
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        CustomerUpdateRequest request = new CustomerUpdateRequest(null, "samer@example.com", null);
        when(customerDao.checkIfEmailExist(request.email())).thenReturn(true);
        assertThatThrownBy(() -> underTest.updateCustomer(request, id)).isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");
    }

    @Test
    void updateCustomerFailsNoChanges() {
        //Given
        var id = 1L;
        Customer customer = new Customer(id, "Bachar", "bachar@example.com", 23);
        //When
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        CustomerUpdateRequest request = new CustomerUpdateRequest(null, null, null);
        assertThatThrownBy(() -> underTest.updateCustomer(request, id)).isInstanceOf(RequestValidationException.class)
                .hasMessage("No data changes found");
    }
}