package com.nexign.bootcamp24.cdr.service;

import com.nexign.bootcamp24.cdr.repo.CustomerRepo;
import com.nexign.bootcamp24.common.domain.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerService {

    //абоненты по умолчанию
    public static final List<String> DEFAULT_CUSTOMERS = Collections.unmodifiableList(new ArrayList<>(
            Arrays.asList("79101952523",
                    "79124307378",
                    "79124307393",
                    "79125317479",
                    "79124157373",
                    "79228986598",
                    "79288394830",
                    "79731104324",
                    "79731703325",
                    "79124519373")));

    private final CustomerRepo customerRepo;
    private final Random random;

    /**
     * Метод генерирует абонентов случайным образом
     * @return список новых пользователей
     */
    public Set<Customer> generateCustomers() {
        Set<Customer> customers = new HashSet<>();

        //создаем абонентов по дефолтным номерам
        DEFAULT_CUSTOMERS.forEach(s -> {
            customers.add(Customer.builder().phone(s).build());
        });

        int count = random.nextInt(15) + 10; //ограничение на кол-во уникальных номеров (по условию не менее 10)
        while (customers.size() < count) {
            customers.add(Customer.builder()
                    .phone(generateRandomNumber()).build());
        }

        return customers;
    }

    private String generateRandomNumber() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("79");

        do {
            stringBuilder.append(random.nextInt(10));
        } while (stringBuilder.length() < 11);

        return stringBuilder.toString();
    }

    /**
     * Сохраняет пользователей в базу
     */
    @Transactional
    public void saveAll(Collection<Customer> customers) {
        customerRepo.saveAll(customers);
    }

    public void deleteAll() {
        customerRepo.deleteAll();
    }

}
