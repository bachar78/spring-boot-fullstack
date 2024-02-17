package com.bachar.utils;


import com.bachar.customer.Customer;
import com.bachar.customer.CustomerRegistrationRequest;
import com.bachar.customer.CustomerUpdateRequest;
import com.github.javafaker.Faker;


import java.util.Random;
import java.util.UUID;

public class GenerateRequests {
    static Faker faker = new Faker();
    static Random random = new Random();

    public static CustomerRegistrationRequest generateRegistrationRequest() {
        String name = faker.name().firstName();
        String email = name + UUID.randomUUID()+"@example.com";
        int age = random.nextInt(18,77);
        return CustomerRegistrationRequest.builder().name(name).email(email).age(age).build();
    }

    public static CustomerUpdateRequest generateCustomerUpdateRequest() {
        String name = faker.name().firstName();
        String email = name + UUID.randomUUID()+"@example.com";
        int age = random.nextInt(18,77);
        return CustomerUpdateRequest.builder().name(name).email(email).age(age).build();
    }

    public static Customer generateCustomer() {
        String name = faker.name().firstName();
        String email = name + UUID.randomUUID()+"@example.com";
        int age = random.nextInt(18,77);
        return Customer.builder().name(name).email(email).age(age).build();
    }

}
