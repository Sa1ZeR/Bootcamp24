package com.nexign.bootcamp24.cdr.service;

import com.nexign.bootcamp24.common.domain.entity.CallTransaction;
import com.nexign.bootcamp24.common.domain.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CallTransactionServiceTest {

    @Autowired
    CallTransactionService callTransactionService;
    @Autowired
    CustomerService customerService;

    @Test
    void generateCdrForCustomers() {
        Set<Customer> customers = customerService.generateCustomers();
        List<CallTransaction> callTransactions = callTransactionService.generateCdrForCustomers(customers);

        //проверим, что для каждого пользователя был создан cdr
        Map<Customer, List<CallTransaction>> grouped = callTransactions.stream().collect(Collectors.groupingBy(CallTransaction::getCustomer));

        assertEquals(grouped.keySet().size(), customers.size());
    }
}