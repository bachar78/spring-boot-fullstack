package com.bachar.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJPADateAccessService implements CustomerDao {

    private final CustomerRepository customerRepository;

    public CustomerJPADateAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public List<Customer> getAllCustomers() {
        Page<Customer> customersPage = customerRepository.findAll(Pageable.ofSize(500));
        return customersPage.getContent();
    }

    @Override
    public Optional<Customer> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean checkIfEmailExist(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public boolean checkCustomerById(Long customerId) {
        return customerRepository.existsCustomerById(customerId);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> findCustomerByEamil(String email) {
        return customerRepository.findCustomerByEmail(email);
    }
}
