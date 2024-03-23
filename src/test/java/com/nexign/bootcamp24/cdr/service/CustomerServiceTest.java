package com.nexign.bootcamp24.cdr.service;

import com.nexign.bootcamp24.common.domain.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    CustomerService customerService;

    @Test
    void generateCustomers() {
        Set<Customer> customers = customerService.generateCustomers();
        //по условию нам надо создать не менее 10 уникальных пользователей
        assertTrue(customers.size() >= 10);
    }
}