package com.nexign.bootcamp24.cdr.service;

import com.nexign.bootcamp24.cdr.repo.CallTransactionRepo;
import com.nexign.bootcamp24.common.domain.entity.CallTransaction;
import com.nexign.bootcamp24.common.domain.entity.Customer;
import com.nexign.bootcamp24.common.domain.enums.CallType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallTransactionService {

    private final CallTransactionRepo repo;
    private final Random random;

    /**
     *
     * @param customers - список абонентов
     * @return список сгенерированных звонков
     */
    public List<CallTransaction> generateCdrForCustomers(Collection<Customer> customers) {
        log.info("Starting generate Call Transaction data");
        List<Customer> customerList = new ArrayList<>(customers);

        List<CallTransaction> data = new ArrayList<>();

        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0); //начинаем создавать данные с начала года

        int transactionCount = customers.size() * 95;
        for(int i = 0; i < transactionCount; i++) {
            if(startDate.getYear() == 2025)
                startDate = LocalDateTime.of(2024, 1, 1, 0, 0);; //если вышли за пределы одного года, обновляем дату

            //генерируем рандомные данные для cdr записи
            CallType callType = CallType.values()[random.nextInt(2)];
            Customer customer = customerList.get(random.nextInt(customerList.size()));
            startDate = startDate.plusSeconds(random.nextInt(3600) + 35);
            LocalDateTime endDate = startDate.plusSeconds(random.nextInt(5000) + 3); //минимум 3 секунды

            data.add(CallTransaction.builder()
                    .callType(callType)
                    .customer(customer)
                    .dateStart(startDate)
                    .dateEnd(endDate)
                    .build());

            startDate = startDate.plusSeconds(random.nextInt(36600)); //прыжок между звонками
        }

        log.info("Call Transaction data successfully generated");

        //отсортируем в хронологическом порядке
        data.sort(Comparator.comparing(CallTransaction::getDateStart));

        return data;
    }

    @Transactional
    public void saveAll(Collection<CallTransaction> transactions) {
        repo.saveAll(transactions);
    }
    @Transactional
    public void deleteAll() {
        repo.deleteAll();
    }
}
