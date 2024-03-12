package com.bachar.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("fake")
public class CustomerDataAccessService implements CustomerDao {
    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer bachar = new Customer(1L, "Bachar", "bachar@example.com", "password", 45, Gender.MALE);
        Customer ali = new Customer(2L, "Ali", "ali@example.com", "password", 33, Gender.MALE);
        customers.add(bachar);
        customers.add(ali);
    }


    @Override
    public List<Customer> getAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> getCustomerById(Long customerId) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(customerId))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean checkIfEmailExist(String email) {
        return customers.stream().anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public boolean checkCustomerById(Long customerId) {
        return customers.stream().anyMatch(customer -> customer.getId().equals(customerId));
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customers.stream().filter(customer -> customer.getId().equals(customerId))
                .findFirst().ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public Optional<Customer> findCustomerByEamil(String email) {
     return customers.stream().filter(customer -> customer.getUsername().equals(email)).findFirst();
    }
}
