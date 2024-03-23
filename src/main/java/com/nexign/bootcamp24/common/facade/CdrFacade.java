package com.nexign.bootcamp24.common.facade;

import com.nexign.bootcamp24.cdr.service.CallTransactionService;
import com.nexign.bootcamp24.cdr.service.CdrService;
import com.nexign.bootcamp24.cdr.service.CustomerService;
import com.nexign.bootcamp24.common.domain.dto.CdrDto;
import com.nexign.bootcamp24.common.domain.entity.CallTransaction;
import com.nexign.bootcamp24.common.domain.entity.Customer;
import com.nexign.bootcamp24.common.mapper.CdrMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CdrFacade {

    private final CallTransactionService transactionService;
    private final CustomerService customerService;
    private final CdrService cdrService;
    private final CdrMapper cdrMapper;

    /**
     * Используется паттерн фасад для управления методами, которые связаны с CDR
     */
    public void cdrStart() {
        log.info("Starting CDR service");
        //запуск генерации новых данных
        Set<Customer> customers = customerService.generateCustomers();
        List<CallTransaction> callTransactions = transactionService.generateCdrForCustomers(customers);

        //сохранение в базу
        transactionService.saveAll(callTransactions);

        //сохранение в файл
        List<CdrDto> cdrList = callTransactions.stream().map(cdrMapper::transactionToCdr).toList();
        cdrService.generateCdrFile(cdrList);

        log.info("CDR service successfully generated data");
    }
}
