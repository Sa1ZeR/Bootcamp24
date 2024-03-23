package com.nexign.bootcamp24.cdr.repo;

import com.nexign.bootcamp24.common.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
}
