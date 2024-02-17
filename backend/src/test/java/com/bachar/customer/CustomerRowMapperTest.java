package com.bachar.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    private CustomerRowMapper underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerRowMapper();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void mapRow() throws SQLException {
        //Given
        Customer customer = new Customer(1L, "bachar", "bachar@example.com", 44);
        ResultSet mockedRS = mock(ResultSet.class);
        when(mockedRS.getLong("id")).thenReturn(1L);
        when(mockedRS.getString("name")).thenReturn("bachar");
        when(mockedRS.getString("email")).thenReturn("bachar@example.com");
        when(mockedRS.getInt("age")).thenReturn(44);
        //When
        Customer customerMapped = underTest.mapRow(mockedRS,1);
        //Then
        assertEquals(customerMapped, customer);
        assertThat(customerMapped.getName()).isEqualTo(customer.getName());
        assertThat(customerMapped.getEmail()).isEqualTo(customer.getEmail());
        assertThat(customerMapped.getAge()).isEqualTo(customer.getAge());
    }
}