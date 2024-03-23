package com.nexign.bootcamp24.cdr.service;

import com.nexign.bootcamp24.common.domain.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    CustomerService customerService;
    @Autowired
    CallTransactionService callTransactionService;

    @Test
    void generateCustomers() {
        Set<Customer> customers = customerService.generateCustomers();
        //по условию нам надо создать не менее 10 уникальных пользователей
        assertTrue(customers.size() >= 10);
    }

    @Test
    void findAll() {
        Set<Customer> customers = customerService.generateCustomers();

        customerService.saveAll(customers);

        List<Customer> all = customerService.findAll();

        assertEquals(customers.size(), all.size());
    }

    @BeforeEach
    void init() {
        //SpringBootTest поднимает приложение целиком, поэтому отчистим базу
        callTransactionService.deleteAll();
        customerService.deleteAll();
    }
}