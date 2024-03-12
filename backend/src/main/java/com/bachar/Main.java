package com.bachar;


import com.bachar.customer.Customer;
import com.bachar.customer.CustomerRepository;
import com.bachar.customer.Gender;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("Application started running");
    }


    @Bean
    CommandLineRunner runner(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder) {
        Faker faker = new Faker();
        var firstName = faker.name().firstName();
        var lastName = faker.name().lastName();
        var email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";
        var password = "password";
        Random random = new Random();
        int age = random.nextInt(16, 99);
        Gender gender = age % 2 ==0? Gender.MALE : Gender.FEMALE;
        return args -> {
            Customer customer = new Customer(firstName + " " + lastName, email, passwordEncoder.encode(password), age, gender);
            customerRepository.save(customer);
        };
    }

}
