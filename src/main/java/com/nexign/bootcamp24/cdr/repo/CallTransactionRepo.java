package com.nexign.bootcamp24.cdr.repo;

import com.nexign.bootcamp24.common.domain.entity.CallTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallTransactionRepo extends JpaRepository<CallTransaction, Long> {
}
