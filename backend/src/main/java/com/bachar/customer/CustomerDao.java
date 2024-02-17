package com.bachar.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(Long customerId);

    void insertCustomer(Customer customer);

    boolean checkIfEmailExist(String email);

    boolean checkCustomerById(Long customerId);

    void deleteCustomer(Long customerId);

    void updateCustomer(Customer customer);

}
