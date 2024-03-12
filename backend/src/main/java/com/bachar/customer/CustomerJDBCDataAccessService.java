package com.bachar.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> getAllCustomers() {
        var sql = """
                SELECT id, name, email, password, age, gender
                FROM customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> getCustomerById(Long customerId) {
        var sql = """
                SELECT id, name, email, password, age, gender
                FROM customer 
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, customerId).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
//     construct the sql first:
        var sql = """
                INSERT INTO customer(name, email, password, age, gender)
                VALUES (?, ?, ?, ?, ?)
                """;

        int result = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getPassword(), customer.getAge(), customer.getGender().name());
    }

    @Override
    public boolean checkIfEmailExist(String email) {
        var sql = """
                SELECT count(*)
                FROM customer 
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null & count > 0;
    }

    @Override
    public boolean checkCustomerById(Long customerId) {
        var sql = """
                SELECT count(*)
                FROM customer 
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, customerId);
        return count != null & count > 0;
    }

    @Override
    public void deleteCustomer(Long customerId) {
        var sql = """
                DELETE 
                FROM customer 
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, customerId);
    }

    @Override
    public void updateCustomer(Customer customer) {
        if (customer.getName() != null) {
            var sql = """
                    UPDATE customer SET name = ? WHERE id = ?
                    """;
            int result = jdbcTemplate.update(sql, customer.getName(), customer.getId());
            System.out.println("update customer name result = " + result);
        }
        if (customer.getEmail() != null) {
            var sql = """
                    UPDATE customer SET email = ? WHERE id = ?
                    """;
            int result = jdbcTemplate.update(sql, customer.getEmail(), customer.getId());
            System.out.println("update customer email result = " + result);
        }
        if (customer.getAge() != null) {
            var sql = """
                    UPDATE customer SET age = ? WHERE id = ?
                    """;
            int result = jdbcTemplate.update(sql, customer.getAge(), customer.getId());
            System.out.println("update customer age result = " + result);
        }
        if (customer.getGender() != null) {
            var sql = """
                    UPDATE customer SET gender = ? WHERE id = ?
                    """;
            int result = jdbcTemplate.update(sql, customer.getGender().name(), customer.getId());
            System.out.println("update customer age result = " + result);
        }
    }

    @Override
    public Optional<Customer> findCustomerByEamil(String email) {
        var sql = """
                SELECT id, name, email, password, age, gender
                FROM customer 
                WHERE email = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, email).stream().findFirst();
    }
}
