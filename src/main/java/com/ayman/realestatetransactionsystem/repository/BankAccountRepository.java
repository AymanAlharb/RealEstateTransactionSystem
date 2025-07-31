package com.ayman.realestatetransactionsystem.repository;

import com.ayman.realestatetransactionsystem.model.entity.BankAccount;
import com.ayman.realestatetransactionsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    BankAccount findBankAccountByUser(User user);
    BankAccount findBankAccountByAccountNumber(String accountNumber);
}
