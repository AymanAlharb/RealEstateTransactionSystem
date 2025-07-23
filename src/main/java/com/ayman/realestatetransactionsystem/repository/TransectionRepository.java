package com.ayman.realestatetransactionsystem.repository;

import com.ayman.realestatetransactionsystem.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransectionRepository extends JpaRepository<Transaction, Long> {
    Transaction findTransactionById(Long id);
}
