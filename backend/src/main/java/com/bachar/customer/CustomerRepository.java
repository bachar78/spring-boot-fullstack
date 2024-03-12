package com.bachar.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
//    @Query("SELECT c from Customer where ...")
    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Long customerId);
    Optional<Customer> findCustomerByEmail(String email);
}
