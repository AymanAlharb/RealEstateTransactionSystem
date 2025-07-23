package com.ayman.realestatetransactionsystem.repository;

import com.ayman.realestatetransactionsystem.model.BankAccount;
import com.ayman.realestatetransactionsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    BankAccount findBankAccountByUser(User user);
}
