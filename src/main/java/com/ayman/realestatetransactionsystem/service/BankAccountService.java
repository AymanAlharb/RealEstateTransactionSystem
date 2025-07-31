package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.model.entity.BankAccount;
import com.ayman.realestatetransactionsystem.model.entity.User;
import com.ayman.realestatetransactionsystem.model.dto.request.CreateBankRequest;
import com.ayman.realestatetransactionsystem.repository.BankAccountRepository;
import com.ayman.realestatetransactionsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Slf4j
@Service
public class BankAccountService {
    private final BankAccountRepository bankRepository;
    private final UserRepository userRepository;
    private final CommonService commonService;

    public void addBankAccount(CreateBankRequest bankRequest) {
        // Get the user
        User user = userRepository.findUserByUsername(commonService.
                getUsernameFromToken(SecurityContextHolder.getContext().getAuthentication()));

        // Check if the user has a bank account
        if (user.getBankAccount() != null) {
            log.info("User: {} tried to add another bank account", user.getUsername());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already has a bank account");
        }
        if(bankRepository.findBankAccountByAccountNumber(bankRequest.getAccountNumber()) != null){
            log.info("User: {} tried to add a bank account with a used account number", user.getUsername());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account number exists in the system");
        }
        // Create
        BankAccount bankAccount = BankAccount.builder()
                .accountNumber(bankRequest.getAccountNumber())
                .balance(bankRequest.getBalance())
                .user(user)
                .build();

        bankRepository.save(bankAccount);
        log.info("User: {} added a bank account", user.getUsername());
    }
}
