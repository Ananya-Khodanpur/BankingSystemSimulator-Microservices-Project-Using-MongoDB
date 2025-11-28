package com.service;

import com.document.Transaction;
import com.document.dto.*;
import com.enums.TransactionStatus;
import com.enums.TransactionType;
import com.exceptions.InvalidException;
import com.repository.TransactionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    RestTemplate restTemplate;

    private static final String ACCOUNT_SERVICE_URL = "http://ACCOUNT-SERVICE/api/accounts/";
    private static final String NOTIFICATION_URL = "http://NOTIFICATION-SERVICE/api/notifications/send";

    Logger logger = LoggerFactory.getLogger(TransactionService.class);

    // Deposit
    @CircuitBreaker(name = "accountService", fallbackMethod = "accountServiceFallback")
    public Transaction deposit(AmountRequestDTO dto) throws InvalidException {

        logger.info("Request Deposit for AccNo {}", dto.getAccountNumber());

        if (dto.getAmount() <= 0) {
            throw new InvalidException("Amount must be greater than zero");
        }

        String url = ACCOUNT_SERVICE_URL + dto.getAccountNumber() + "/balance";
        UpdateBalanceRequestDTO updateDto = new UpdateBalanceRequestDTO(dto.getAmount());

        ResponseEntity<ApiResponse> response = restTemplate.exchange(
                url, HttpMethod.PUT, new HttpEntity<>(updateDto), ApiResponse.class);

        ApiResponse res = response.getBody();

        if (res == null || !res.isSuccess()) {
            logger.error("Balance update FAILED! for AccNo {}", dto.getAccountNumber());
            throw new InvalidException(res != null ? res.getMessage() : "");
        }

        Transaction t = new Transaction();
        t.setTransactionId(generateTransactionId());
        t.setAmount(dto.getAmount());
        t.setStatus(TransactionStatus.SUCCESS);
        t.setType(TransactionType.DEPOSIT);
        t.setTimestamp(LocalDateTime.now());
        t.setSourceAccount(dto.getAccountNumber());
        t.setDestinationAccount(null);

        transactionRepository.save(t);

        notify(dto.getAccountNumber(),
                dto.getAmount() + " successfully deposited to " + dto.getAccountNumber());

        logger.info("Successful deposit for AccNo {}", dto.getAccountNumber());

        return t;
    }

    public static String generateTransactionId() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        int rand = new Random().nextInt(999) + 1;
        return "TXN-" + date + "-" + rand;
    }

    // Withdraw
    @CircuitBreaker(name = "accountService", fallbackMethod = "accountServiceFallback")
    public Transaction withdraw(AmountRequestDTO dto) throws InvalidException {

        logger.info("Request Withdraw for AccNo {}", dto.getAccountNumber());

        if (dto.getAmount() <= 0) {
            throw new InvalidException("Amount must be greater than 0");
        }

        String url = ACCOUNT_SERVICE_URL + dto.getAccountNumber() + "/balance";
        UpdateBalanceRequestDTO updateDto = new UpdateBalanceRequestDTO(-dto.getAmount());

        ResponseEntity<ApiResponse> response = restTemplate.exchange(
                url, HttpMethod.PUT, new HttpEntity<>(updateDto), ApiResponse.class);

        ApiResponse res = response.getBody();

        if (res == null || !res.isSuccess()) {
            logger.error("Balance update FAILED! for AccNo {}", dto.getAccountNumber());
            throw new InvalidException(res != null ? res.getMessage() : "");
        }

        Transaction t = new Transaction();
        t.setTransactionId(generateTransactionId());
        t.setAmount(dto.getAmount());
        t.setStatus(TransactionStatus.SUCCESS);
        t.setType(TransactionType.WITHDRAW);
        t.setTimestamp(LocalDateTime.now());
        t.setSourceAccount(null);
        t.setDestinationAccount(dto.getAccountNumber());

        transactionRepository.save(t);

        notify(dto.getAccountNumber(),
                dto.getAmount() + " successfull withdraw from " + dto.getAccountNumber());

        logger.info("Successful withdraw for AccNo {}", dto.getAccountNumber());

        return t;
    }

    // Transfer
    @CircuitBreaker(name = "accountService", fallbackMethod = "accountServiceTransferFallback")
    public Transaction transfer(TransferRequestDTO dto) throws InvalidException {

        logger.info("Request Transfer from Source AccNo {} to  Destination AccNo {}", dto.getFromAccount(), dto.getToAccount());

        if (dto.getAmount() <= 0) {
            throw new InvalidException("Amount must be greater than 0");
        }

        String fromURL = ACCOUNT_SERVICE_URL + dto.getFromAccount() + "/balance";
        String toURL = ACCOUNT_SERVICE_URL + dto.getToAccount() + "/balance";

        ResponseEntity<ApiResponse> response1 = restTemplate.exchange(
                fromURL, HttpMethod.PUT,
                new HttpEntity<>(new UpdateBalanceRequestDTO(-dto.getAmount())),
                ApiResponse.class);

        ApiResponse res1 = response1.getBody();
        if (res1 == null || !res1.isSuccess()) {
            logger.error("Debit failed for {}", dto.getFromAccount());
            throw new InvalidException(res1 != null ? res1.getMessage() : "");
        }

        ResponseEntity<ApiResponse> response2 = restTemplate.exchange(
                toURL, HttpMethod.PUT,
                new HttpEntity<>(new UpdateBalanceRequestDTO(dto.getAmount())),
                ApiResponse.class);

        ApiResponse res2 = response2.getBody();
        if (res2 == null || !res2.isSuccess()) {
            logger.error("Credit failed for {}", dto.getToAccount());
            throw new InvalidException(res2 != null ? res2.getMessage() : "");
        }

        Transaction t = new Transaction();
        t.setTransactionId(generateTransactionId());
        t.setAmount(dto.getAmount());
        t.setStatus(TransactionStatus.SUCCESS);
        t.setType(TransactionType.TRANSFER);
        t.setTimestamp(LocalDateTime.now());
        t.setSourceAccount(dto.getFromAccount());
        t.setDestinationAccount(dto.getToAccount());

        transactionRepository.save(t);

        notify(dto.getFromAccount(),
                dto.getAmount() + " successfully transferred to " + dto.getToAccount());
        notify(dto.getToAccount(),
                dto.getAmount() + " successfully received from " + dto.getFromAccount());

        logger.info("Transfer successful from {} to {}", dto.getFromAccount(), dto.getToAccount());

        return t;
    }

    // Get all transactions
    public List<Transaction> getAllTransactions(String accNo) {

        logger.info("Fetching all transactions for {}", accNo);

        return transactionRepository.findBySourceAccountOrDestinationAccount(accNo, accNo);
    }

    // Notify
    @CircuitBreaker(name = "notificationService", fallbackMethod = "notificationFallback")
    private void notify(String accNo, String message) {
        NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO(accNo, message);

        try {
            restTemplate.postForObject(NOTIFICATION_URL, notificationRequestDTO, String.class);
        } catch (Exception ex) {
            notificationFallback(accNo, message, ex);
        }
    }

    // Fallback (deposit/withdraw)
    public Transaction accountServiceFallback(AmountRequestDTO dto, InvalidException ex) {

        throw ex;
    }

    public Transaction accountServiceFallback(AmountRequestDTO dto, Throwable ex) {
        logger.error("Account Service is DOWN: {}", ex.getMessage());

        Transaction t = new Transaction();
        t.setTransactionId(generateTransactionId());
        t.setAmount(0.0);
        t.setStatus(TransactionStatus.FAILED);
        t.setTimestamp(LocalDateTime.now());

        return t;
    }

    // Fallback (transfer)
    public Transaction accountServiceTransferFallback(TransferRequestDTO dto, InvalidException ex) {

        throw ex;
    }

    public Transaction accountServiceTransferFallback(TransferRequestDTO dto, Throwable ex) {
        logger.error("Account Service is DOWN: {}", ex.getMessage());

        Transaction t = new Transaction();
        t.setTransactionId(generateTransactionId());
        t.setAmount(0.0);
        t.setStatus(TransactionStatus.FAILED);
        t.setTimestamp(LocalDateTime.now());

        return t;
    }

    // Notification fallback
    public void notificationFallback(String accNo, String message, Throwable ex) {
        logger.warn("Notification Service is DOWN: {}", ex.getMessage());
    }
}
