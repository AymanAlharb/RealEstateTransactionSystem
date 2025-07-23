package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.model.BankAccount;
import com.ayman.realestatetransactionsystem.model.User;
import com.ayman.realestatetransactionsystem.model.dto.CreateBankRequest;
import com.ayman.realestatetransactionsystem.repository.BankAccountRepository;
import com.ayman.realestatetransactionsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class BankAccountService {
    private final BankAccountRepository bankRepository;
    private final UserRepository userRepository;
    private final CommonService commonService;

    public void addBankAccount(CreateBankRequest bankRequest){
        // Get the user
        User user = userRepository.findUserByUsername(commonService.
                getUsernameFromToken(SecurityContextHolder.getContext().getAuthentication()));

        // Create
        BankAccount bankAccount = BankAccount.builder()
                .accountNumber(bankRequest.getAccountNumber())
                .balance(bankRequest.getBalance())
                .user(user)
                .build();

        bankRepository.save(bankAccount);
    }
}
