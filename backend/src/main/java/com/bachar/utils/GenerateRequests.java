package com.bachar.utils;


import com.bachar.customer.Customer;
import com.bachar.customer.CustomerRegistrationRequest;
import com.bachar.customer.CustomerUpdateRequest;
import com.bachar.customer.Gender;
import com.github.javafaker.Faker;


import java.util.Random;
import java.util.UUID;

public class GenerateRequests {
    static Faker faker = new Faker();
    static Random random = new Random();

    public static CustomerRegistrationRequest generateRegistrationRequest() {
        String name = faker.name().firstName();
        String email = name + UUID.randomUUID() + "@example.com";
        int age = random.nextInt(18, 77);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        String password = "password";
        return new CustomerRegistrationRequest(name, email, password, age, gender);
    }

    public static CustomerUpdateRequest generateCustomerUpdateRequest() {
        String name = faker.name().firstName();
        String email = name + UUID.randomUUID() + "@example.com";
        int age = random.nextInt(18, 77);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        return new CustomerUpdateRequest(name, email, age, gender);
    }

}
