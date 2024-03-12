package com.bachar.customer;

import com.bachar.exception.RequestValidationException;
import com.bachar.exception.DuplicateResourceException;
import com.bachar.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final CustomerDTOMapper customerDTOMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao, CustomerDTOMapper customerDTOMapper, PasswordEncoder passwordEncoder) {
        this.customerDao = customerDao;
        this.customerDTOMapper = customerDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomerDTO> selectAllCustomers() {
        return customerDao.getAllCustomers().stream().map(customerDTOMapper).collect(Collectors.toList());
    }

    public CustomerDTO getCustomer(Long customerId) {
        return customerDao.getCustomerById(customerId).map(customerDTOMapper)
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
                        passwordEncoder.encode(request.password()),
                        request.age(),
                        request.gender())
        );
    }

    public void removeCustomer(Long customerId) {
        if (!customerDao.checkCustomerById(customerId)) {
            throw new ResourceNotFoundException("Customer with id [%s] is not found".formatted(customerId));
        }
        customerDao.deleteCustomer(customerId);
    }

    public void updateCustomer(CustomerUpdateRequest request, Long customerId) {
        boolean update = false;
        Customer customer = customerDao.getCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id [%s] is not found".formatted(customerId)));
        if (request.name() != null && !request.name().equals(customer.getName())) {
            customer.setName(request.name());
            update = true;
        }
        if (request.email() != null && !request.email().equals(customer.getEmail())) {
            if (customerDao.checkIfEmailExist(request.email())) {
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(request.email());
            update = true;
        }
        if (request.age() != null && !request.age().equals(customer.getAge())) {
            customer.setAge(request.age());
            update = true;
        }
        if (request.gender() != null && !request.gender().equals(customer.getGender())) {
            customer.setGender(request.gender());
            update = true;
        }
        if (!update) {
            throw new RequestValidationException("No data changes found");
        }
        customerDao.updateCustomer(customer);
    }
}
