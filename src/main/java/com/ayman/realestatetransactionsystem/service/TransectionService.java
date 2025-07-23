package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.exception.ApiException;
import com.ayman.realestatetransactionsystem.model.*;
import com.ayman.realestatetransactionsystem.model.dto.CreateApprovalRequest;
import com.ayman.realestatetransactionsystem.model.dto.PaymentRequest;
import com.ayman.realestatetransactionsystem.model.enums.TransectionStatusEnum;
import com.ayman.realestatetransactionsystem.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class TransectionService {
    private final TransectionRepository transectionRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final CommonService commonService;
    private final BankAccountRepository bankRepository;
    private final PropertyOwnerShipRepository ownerShipRepository;

    public void requestProperty(Long propertyId) {
        // Get buyer
        User buyer = userRepository.findUserByUsername(commonService.
                getUsernameFromToken(SecurityContextHolder.getContext().getAuthentication()));

        // Get the property
        Property property = propertyRepository.findPropertyById(propertyId);
        if (property == null)
            throw new ApiException("No property with the id " + propertyId + " exists");

        Transaction transaction = Transaction.builder()
                .amount(property.getPrice())
                .status(TransectionStatusEnum.PENDING)
                .date(LocalDateTime.now())
                .property(property)
                .buyer(buyer)
                .seller(property.getOwner())
                .broker(property.getBroker())
                .build();

        log.info("Buyer : {} has made a request to buy {}", buyer.getUsername(), property.getTitle());

        transectionRepository.save(transaction);

        // TODO : Notification to seller
    }

    public void sellerApproveOrDissApprove(CreateApprovalRequest approvalRequest) {
        // Get transection
        Transaction transaction = getTransectionOrThrow(approvalRequest);

        // Get seller
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User seller = userRepository.findUserByUsername(commonService.getUsernameFromToken(auth));

        // Check if the property belongs to the seller
        if (!transaction.getProperty().getOwner().equals(seller)) {
            log.info("{} tried to access the {} property", seller.getUsername(), transaction.getProperty().getTitle());
            throw new ApiException("Property does not belong to seller");
        }

        if (approvalRequest.getApproval()) {
            transaction.setStatus(TransectionStatusEnum.APPROVED_BY_SELLER);
            transectionRepository.save(transaction);
            log.info("{} approved the transection with the id {}", seller.getUsername(), approvalRequest.getTransectionId());
            // TODO : email notification
        } else {
            transaction.setStatus(TransectionStatusEnum.FAILED);
            transaction.setReasonOfFailure(String.valueOf(approvalRequest.getReasonOfFailure()));
            log.info("{} unapproved the transection with the id {}", seller.getUsername(), approvalRequest.getTransectionId());
        }
    }

    public void brokerApproveOrDissApprove(CreateApprovalRequest approvalRequest) {
        // Get transection
        Transaction transaction = getTransectionOrThrow(approvalRequest);

        // Get broker
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User broker = userRepository.findUserByUsername(commonService.getUsernameFromToken(auth));

        // Check if the broker has authorities on the property
        if (!transaction.getProperty().getBroker().equals(broker)) {
            log.info("{} tried to access the {} property", broker.getUsername(), transaction.getProperty().getTitle());
            throw new ApiException("Broker has no authorities on the property");
        }

        if (approvalRequest.getApproval()) {
            transaction.setStatus(TransectionStatusEnum.APPROVED);
            transectionRepository.save(transaction);
            log.info("{} approved the transection with the id {}", broker.getUsername(), approvalRequest.getTransectionId());
        } else {
            transaction.setStatus(TransectionStatusEnum.FAILED);
            transaction.setReasonOfFailure(String.valueOf(approvalRequest.getReasonOfFailure()));
            log.info("{} unapproved the transection with the id {}", broker.getUsername(), approvalRequest.getTransectionId());
        }
    }

    public void payment(PaymentRequest paymentRequest) {
        // Get Transection
        Transaction transaction = transectionRepository.findTransactionById(paymentRequest.getTransectionId());
        if (transaction == null)
            throw new ApiException("No transection with the id " + paymentRequest.getTransectionId() + " exists");

        // Get buyer
        User buyer = userRepository.findUserByUsername(commonService.
                getUsernameFromToken(SecurityContextHolder.getContext().getAuthentication()));

        // Check if transection belongs to buyer
        if (!transaction.getBuyer().equals(buyer))
            throw new ApiException("The transection does not belongs to the buyer");

        // Check transection status
        if (!transaction.getStatus().equals(TransectionStatusEnum.APPROVED))
            throw new ApiException("Transection not approved or completed");

        // Get bank accounts
        BankAccount bankAccount = getBankAccountOrThrow(buyer);
        BankAccount brokerAccount = getBankAccountOrThrow(transaction.getBroker());
        BankAccount sellerAccount = getBankAccountOrThrow(transaction.getSeller());

        double price = transaction.getAmount();

        // Check and deduct balance
        if (price > bankAccount.getBalance())
            throw new ApiException("Insufficient balance");
        buyer.getBankAccount().setBalance(buyer.getBankAccount().getBalance() - price);
        userRepository.save(buyer);

        // Transfer to broker
        brokerAccount.setBalance(brokerAccount.getBalance() + (price * 0.049375));

        price -= price * 0.049375;

        // Transfer to seller
        sellerAccount.setBalance(sellerAccount.getBalance() + price);

        userRepository.save(transaction.getBroker());
        userRepository.save(transaction.getSeller());

        // Create ownership object
        PropertyOwnership ownership = PropertyOwnership.builder()
                .ownerFlag(true)
                .ownershipDate(LocalDateTime.now())
                .property(transaction.getProperty())
                .owner(buyer)
                .build();

        ownerShipRepository.save(ownership);

        // Transfer ownership
        PropertyOwnership oldOwnerShip = ownerShipRepository.getPropertyOwnershipByPropertyAndOwner(transaction.getProperty(), transaction.getSeller());
        oldOwnerShip.setOwnerFlag(false);
        ownerShipRepository.save(oldOwnerShip);
        transaction.getProperty().setOwner(buyer);
        propertyRepository.save(transaction.getProperty());

        // Change transection status.
        transaction.setStatus(TransectionStatusEnum.COMPLETED);
        transectionRepository.save(transaction);

    }

    private BankAccount getBankAccountOrThrow(User user) {
        BankAccount bankAccount = bankRepository.findBankAccountByUser(user);
        if (bankAccount == null)
            throw new ApiException("No bank account for " + user.getUsername());
        return bankAccount;
    }

    private Transaction getTransectionOrThrow(CreateApprovalRequest approvalRequest) {
        Transaction transaction = transectionRepository.findTransactionById(approvalRequest.getTransectionId());
        if (transaction == null)
            throw new ApiException("No transection with the id " + approvalRequest.getTransectionId() + " exists");
        return transaction;
    }
}
