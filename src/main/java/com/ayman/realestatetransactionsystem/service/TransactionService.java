package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.model.dto.request.CreateApprovalRequest;
import com.ayman.realestatetransactionsystem.model.struct.EmailStruct;
import com.ayman.realestatetransactionsystem.model.dto.request.PaymentRequest;
import com.ayman.realestatetransactionsystem.model.entity.*;
import com.ayman.realestatetransactionsystem.model.enums.PropertyStatusEnum;
import com.ayman.realestatetransactionsystem.model.enums.TransectionStatusEnum;
import com.ayman.realestatetransactionsystem.properties.RabbitMQProperties;
import com.ayman.realestatetransactionsystem.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static com.ayman.realestatetransactionsystem.constant.EmailConstant.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@RequiredArgsConstructor
@Slf4j
@Service
public class TransactionService {
    private final TransectionRepository transectionRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final CommonService commonService;
    private final BankAccountRepository bankRepository;
    private final PropertyOwnerShipRepository ownerShipRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;

    @Transactional
    public void requestProperty(Long propertyId) {
        // Get buyer
        User buyer = userRepository.findUserByUsername(commonService.
                getUsernameFromToken(SecurityContextHolder.getContext().getAuthentication()));

        // Get the property
        Property property = propertyRepository.findPropertyById(propertyId);
        if (property == null) {
            log.info("User: {} searched for not existing property", buyer.getUsername());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No property with the id " + propertyId + " exists");
        }

        // Check the property status
        checkPropertyStatus(property, buyer.getUsername());

        Transaction transaction = Transaction.builder()
                .amount(property.getPrice())
                .status(TransectionStatusEnum.PENDING)
                .date(LocalDateTime.now())
                .property(property)
                .buyer(buyer)
                .seller(property.getOwner())
                .broker(property.getBroker())
                .build();

        log.info("Buyer: {} has made a request to buy: {}", buyer.getUsername(), property.getTitle());
        property.setStatus(PropertyStatusEnum.LOCKED);
        propertyRepository.save(property);
        transectionRepository.save(transaction);

        // Send emails
        sendEmail(buyer, String.format(REQUEST_EMAIL_SUBJECT, property.getTitle()),
                String.format(BUYER_REQUEST_MESSAGE, property.getTitle()));
        sendEmail(property.getOwner(), String.format(REQUEST_EMAIL_SUBJECT, property.getTitle()),
                String.format(SELLER_REQUEST_MESSAGE, buyer.getUsername(), property.getTitle()));
    }

    public void sellerApproveOrDissApprove(CreateApprovalRequest approvalRequest) {
        // Get transection
        Transaction transaction = getTransectionOrThrow(approvalRequest);

        // Get seller
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User seller = userRepository.findUserByUsername(commonService.getUsernameFromToken(auth));

        // Check if the property belongs to the seller
        if (!transaction.getProperty().getOwner().equals(seller)) {
            log.info("User: {} tried to approve a request to the property: {} they do not own",
                    seller.getUsername(), transaction.getProperty().getTitle());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Property does not belong to seller");
        }

        // Check if transection status
        if (!transaction.getStatus().equals(TransectionStatusEnum.PENDING)) {
            log.info("User: {} tried to approve a request to the property: {} that can not be approved that is because {}",
                    seller.getUsername(), transaction.getProperty().getTitle(), transaction.getProperty().getStatus());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transection " + transaction.getStatus().toString().toLowerCase() + " and can not be approved by the seller");
        }

        if (approvalRequest.getApproval()) {
            // Modify transaction
            transaction.setStatus(TransectionStatusEnum.APPROVED_BY_SELLER);
            transectionRepository.save(transaction);

            // Send emails
            sendEmail(transaction.getBuyer(), REQUEST_APPROVAL_EMAIL_SUBJECT,
                    String.format(BUYER_APPROVAL_MESSAGE, transaction.getProperty().getTitle(), seller.getUsername()));
            sendEmail(transaction.getBroker(), String.format(REQUEST_EMAIL_SUBJECT, transaction.getProperty().getTitle()), String.format(BROKER_REQUEST_MESSAGE, transaction.getBuyer().getUsername(), transaction.getProperty().getTitle()));

            log.info("User: {} approved the transaction with the id: {}", seller.getUsername(), approvalRequest.getTransectionId());
        } else {
            // Modify transaction
            transaction.setStatus(TransectionStatusEnum.FAILED);
            transaction.setReasonOfFailure(String.valueOf(approvalRequest.getReasonOfFailure()));

            // Send email
            sendEmail(transaction.getBuyer(), REQUEST_DENIED_EMAIL_SUBJECT,
                    String.format(BUYER_DENIAL_MESSAGE, transaction.getProperty().getTitle(), seller, approvalRequest.getReasonOfFailure()));

            log.info("User: {} unapproved the transaction with the id: {}", seller.getUsername(), approvalRequest.getTransectionId());
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
            log.info("User: {} tried to approve a request to the property: {} that they do not have authorities on",
                    broker.getUsername(), transaction.getProperty().getTitle());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Broker has no authorities on the property");
        }

        // Check if seller approved
        if (!transaction.getStatus().equals(TransectionStatusEnum.APPROVED_BY_SELLER)) {
            log.info("User: {} tried to approve a request to the property: {} that can not be approved, that is because {}",
                    broker.getUsername(), transaction.getProperty().getTitle(), transaction.getProperty().getStatus());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction " + transaction.getStatus() + " and can not be approved by the broker");
        }

        if (approvalRequest.getApproval()) {
            // Modify transaction
            transaction.setStatus(TransectionStatusEnum.APPROVED);
            transectionRepository.save(transaction);

            // Send email
            sendEmail(transaction.getBuyer(), REQUEST_APPROVAL_EMAIL_SUBJECT,
                    String.format(BUYER_APPROVAL_MESSAGE, transaction.getProperty().getTitle(), broker.getUsername()));

            log.info("User: {} approved the transection with the id: {}", broker.getUsername(), approvalRequest.getTransectionId());
        } else {
            // Modify transaction
            transaction.setStatus(TransectionStatusEnum.FAILED);
            transaction.setReasonOfFailure(approvalRequest.getReasonOfFailure());

            // Send email
            sendEmail(transaction.getBuyer(), REQUEST_DENIED_EMAIL_SUBJECT,
                    String.format(BUYER_DENIAL_MESSAGE, transaction.getProperty().getTitle(), broker, approvalRequest.getReasonOfFailure()));

            log.info("User: {} unapproved the transection with the id: {}", broker.getUsername(), approvalRequest.getTransectionId());
        }
    }

    public void payment(PaymentRequest paymentRequest) {

        // Get buyer
        User buyer = userRepository.findUserByUsername(commonService.
                getUsernameFromToken(SecurityContextHolder.getContext().getAuthentication()));

        // Get Transection
        Transaction transaction = transectionRepository.findTransactionById(paymentRequest.getTransectionId());

        // Check if transection belongs to buyer and check transaction status
        validateTransectionPayments(buyer, transaction, paymentRequest);

        // Transfer
        transferPrice(buyer, transaction);
        userRepository.save(transaction.getBroker());
        userRepository.save(transaction.getSeller());

        // Create ownership object
        PropertyOwnership ownership = createOwnerShip(buyer, transaction);

        ownerShipRepository.save(ownership);
        log.info("New ownership created for the user: {} and the property: {}", ownership.getOwner().getUsername(), ownership.getProperty().getTitle());

        // Transfer ownership
        completeTransaction(transaction, buyer, ownership);

    }

    private void completeTransaction(Transaction transaction, User buyer, PropertyOwnership ownership) {
        PropertyOwnership oldOwnerShip = ownerShipRepository.getPropertyOwnershipByPropertyAndOwner(transaction.getProperty(), transaction.getSeller());
        oldOwnerShip.setOwnerFlag(false);
        ownerShipRepository.save(oldOwnerShip);

        transaction.getProperty().setOwner(buyer);
        transaction.getProperty().setStatus(PropertyStatusEnum.SOLD);
        propertyRepository.save(transaction.getProperty());
        log.info("Ownership changed for the property: {} from: {} to: {}",
                transaction.getProperty(), oldOwnerShip.getOwner().getUsername(), ownership.getOwner().getUsername());

        // Change transection status.
        transaction.setStatus(TransectionStatusEnum.COMPLETED);

        transectionRepository.save(transaction);
    }

    private void sendEmail(User receiver, String subject, String body) {
        EmailStruct emailStruct = new EmailStruct(receiver, subject, body);
        rabbitTemplate.convertAndSend(rabbitMQProperties.getExchangeName(), rabbitMQProperties.getEmailQueue().getRoutingKeyName(), emailStruct);
    }

    private void validateTransectionPayments(User buyer, Transaction transaction, PaymentRequest paymentRequest) {
        if (transaction == null) {
            log.info("User: {} tried to pay for a non existing transaction", buyer.getUsername());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transection with the id " + paymentRequest.getTransectionId() + " exists");
        }
        // Check the card expiry date
        if (paymentRequest.getExpiryDate().isBefore(LocalDate.now())) {
            log.info("User: {} tried to pay for the transaction: {} with an expired card", buyer.getUsername(), transaction.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card expired");
        }

        // Check if transection belongs to buyer
        if (!transaction.getBuyer().equals(buyer)) {
            log.info("User: {} tried to pay for the transaction: {} which they do not own", buyer.getUsername(), transaction.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The transection does not belongs to the buyer");
        }
        // Check transection status
        if (!transaction.getStatus().equals(TransectionStatusEnum.APPROVED)) {
            log.info("User: {} tried to pay for the transaction: {} but failed due: {}", buyer.getUsername(), transaction.getId(), transaction.getStatus().toString().toLowerCase());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transection: " + transaction.getStatus().toString().toLowerCase());
        }

    }

    private PropertyOwnership createOwnerShip(User buyer, Transaction transaction) {
        return PropertyOwnership.builder()
                .ownerFlag(true)
                .ownershipDate(LocalDateTime.now())
                .property(transaction.getProperty())
                .owner(buyer)
                .build();
    }

    private BankAccount getBankAccountOrThrow(User user) {
        BankAccount bankAccount = bankRepository.findBankAccountByUser(user);
        if (bankAccount == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No bank account for " + user.getUsername());
        return bankAccount;
    }

    private Transaction getTransectionOrThrow(CreateApprovalRequest approvalRequest) {
        Transaction transaction = transectionRepository.findTransactionById(approvalRequest.getTransectionId());
        if (transaction == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transection with the id " + approvalRequest.getTransectionId() + " exists");
        return transaction;
    }

    private void checkPropertyStatus(Property property, String username) {
        switch (property.getStatus()) {
            case HIDDEN -> {
                log.info("User: {} requested the hidden property: {}", username, property.getTitle());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This is a hidden property");
            }
            case LOCKED -> {
                log.info("User: {} requested the locked property: {}", username, property.getTitle());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This is a locked property");
            }
            case SOLD -> {
                log.info("User: {} requested the sold property: {}", username, property.getTitle());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This is a sold property");
            }
        }
    }

    private void transferPrice(User buyer, Transaction transaction) {
        // Get bank accounts
        BankAccount bankAccount = getBankAccountOrThrow(buyer);
        BankAccount brokerAccount = getBankAccountOrThrow(transaction.getBroker());
        BankAccount sellerAccount = getBankAccountOrThrow(transaction.getSeller());

        double price = transaction.getAmount();

        // Check and deduct balance
        if (price > bankAccount.getBalance()) {
            log.info("User: {} failed to pay for the transaction: {} due to insufficient balance", buyer.getUsername(), transaction.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
        }

        buyer.getBankAccount().setBalance(buyer.getBankAccount().getBalance() - price);
        userRepository.save(buyer);
        // Transfer to seller
        sellerAccount.setBalance(sellerAccount.getBalance() + price);
        log.info("{} transferred to the seller: {} from the buyer: {} for the transaction: {}"
                , price, sellerAccount.getUser().getUsername(), buyer.getUsername(), transaction.getId());

        sendEmail(buyer, PAYMENT_EMAIL_SUBJECT, String.format(BUYER_PAYMENT_MESSAGE, transaction.getProperty().getTitle()));
        sendEmail(transaction.getSeller(), PAYMENT_EMAIL_SUBJECT, String.format(SELLER_PAYMENT_MESSAGE, buyer.getUsername(), transaction.getProperty().getTitle()));

        double brokerCommission = price * 0.049375;

        // Deduct from seller
        sellerAccount.setBalance(sellerAccount.getBalance() - brokerCommission);
        // Transfer to broker
        brokerAccount.setBalance(brokerAccount.getBalance() + brokerCommission);

        log.info("{} transferred from: {} account to: {} account"
                , brokerCommission, sellerAccount.getUser().getUsername(), brokerAccount.getUser().getUsername());
        sendEmail(transaction.getBroker(), PAYMENT_EMAIL_SUBJECT, String.format(BROKER_PAYMENT_MESSAGE, transaction.getProperty().getTitle(), brokerCommission));

    }
}
