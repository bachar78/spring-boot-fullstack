package com.bachar.customer;

import com.bachar.exception.RequestValidationException;
import com.bachar.exception.DuplicateResourceException;
import com.bachar.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> selectAllCustomers() {
        return customerDao.getAllCustomers();
    }

    public Customer getCustomer(Long customerId) {
        return customerDao.getCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id [%s] is not found".formatted(customerId)));
    }

    public void addCustomer(CustomerRegistrationRequest request) {
        if (customerDao.checkIfEmailExist(request.email())) {
            throw new DuplicateResourceException("Email already taken");
        }
        customerDao.insertCustomer(
                new Customer(
                        request.name(),
                        request.email(),
                        request.age()
                )
        );
    }

    public void removeCustomer(Long customerId) {
        if (! customerDao.checkCustomerById(customerId)) {
            throw new ResourceNotFoundException("Customer with id [%s] is not found".formatted(customerId));
        }
        customerDao.deleteCustomer(customerId);
    }

    public void updateCustomer(CustomerUpdateRequest request, Long customerId) {
        boolean update = false;
        Customer customer = getCustomer(customerId);
        if(request.name() != null && !request.name().equals(customer.getName())) {
            customer.setName(request.name());
            update = true;
        }
        if(request.email() != null && !request.email().equals(customer.getEmail())) {
            if (customerDao.checkIfEmailExist(request.email())) {
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(request.email());
            update = true;
        }
        if(request.age() != null && !request.age().equals(customer.getAge())) {
            customer.setAge(request.age());
            update = true;
        }
        if(!update) {
            throw new RequestValidationException("No data changes found");
        }
        customerDao.updateCustomer(customer);
    }
}
