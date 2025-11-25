package com.service;

import com.document.Account;
import com.document.dto.AccountRequestDTO;
import com.document.dto.AccountResponseDTO;
import com.document.dto.UpdateBalanceRequestDTO;
import com.enums.AccountStatus;
import com.exceptions.AccountNotFoundException;
import com.exceptions.InvalidAccountStatusException;
import com.exceptions.InvalidBalnceException;
import com.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    final Logger logger = LoggerFactory.getLogger(AccountService.class);

    // Create account
    public AccountResponseDTO createAccount(AccountRequestDTO dto) {

        logger.info("Request for Account creation {}", dto.getHolderName());

        Account acc = new Account();

        String accNo = getAccountNumber(dto.getHolderName());
        while (accountRepository.existsByAccountNumber(accNo)) {
            accNo = getAccountNumber(dto.getHolderName());
        }

        acc.setHolderName(dto.getHolderName());
        acc.setAccountNumber(accNo);
        acc.setBalance(0.0);
        acc.setStatus(AccountStatus.ACTIVE);
        acc.setCreatedAt(LocalDateTime.now());
        accountRepository.save(acc);

        logger.info("Account successfully created, and Account number is {}", accNo);

        // Inline mapping
        AccountResponseDTO response = new AccountResponseDTO();
        response.setAccountNumber(acc.getAccountNumber());
        response.setHolderName(acc.getHolderName());
        response.setId(acc.getId());
        response.setStatus(acc.getStatus());
        response.setBalance(acc.getBalance());
        response.setCreatedAt(acc.getCreatedAt());

        return response;
    }

    public static String getAccountNumber(String holderName) {
        String start = holderName.length() > 3 ?
                holderName.toUpperCase().substring(0, 3) :
                holderName.toUpperCase();

        int rand = new Random().nextInt(9000) + 1000;
        return "ACCXX" + start + rand;
    }

    // Update balance
    public AccountResponseDTO updateBalance(String accNo, UpdateBalanceRequestDTO dto) {

        logger.info("Request for updating balance for account {}", accNo);

        Account acc = accountRepository.findByAccountNumber(accNo)
                .orElseThrow(() -> {
                    logger.error("Unable to find Account with account number {}", accNo);
                    return new AccountNotFoundException("Account not found " + accNo);
                });

        if (acc.getStatus().equals(AccountStatus.INACTIVE)) {
            throw new InvalidAccountStatusException("Please activate your account to update balance");
        }

        double newBalance = acc.getBalance() + dto.getAmount();
        if (newBalance <= 0) {
            throw new InvalidBalnceException("Insufficient balance found!");
        }

        acc.setBalance(newBalance);
        Account saved = accountRepository.save(acc);

        logger.info("Successfully balance is updated for account {}", accNo);

        // Inline mapping
        AccountResponseDTO response = new AccountResponseDTO();
        response.setAccountNumber(saved.getAccountNumber());
        response.setHolderName(saved.getHolderName());
        response.setId(saved.getId());
        response.setStatus(saved.getStatus());
        response.setBalance(saved.getBalance());
        response.setCreatedAt(saved.getCreatedAt());

        return response;
    }

    // Change account status
    public AccountResponseDTO changeStatus(String accNo) {

        logger.info("Request to change Account status {}", accNo);

        Account acc = accountRepository.findByAccountNumber(accNo)
                .orElseThrow(() -> {
                    logger.error("Unable to find Account with account number {}", accNo);
                    return new AccountNotFoundException("Account not found" + accNo);
                });

        acc.setStatus(acc.getStatus().equals(AccountStatus.ACTIVE) ?
                AccountStatus.INACTIVE : AccountStatus.ACTIVE);

        accountRepository.save(acc);

        logger.info("Successfully changed Account status {}", accNo);

        // Inline mapping
        AccountResponseDTO response = new AccountResponseDTO();
        response.setAccountNumber(acc.getAccountNumber());
        response.setHolderName(acc.getHolderName());
        response.setId(acc.getId());
        response.setStatus(acc.getStatus());
        response.setBalance(acc.getBalance());
        response.setCreatedAt(acc.getCreatedAt());

        return response;
    }

    // Get account details
    public AccountResponseDTO getAccountDetails(String accNo) {

        logger.info("Request for Account details {}", accNo);

        Account acc = accountRepository.findByAccountNumber(accNo)
                .orElseThrow(() -> {
                    logger.error("Unable to find Account with account number {}", accNo);
                    return new AccountNotFoundException("Account not found " + accNo);
                });

        logger.info("Successfuly fetched account details for {}", accNo);

        // Inline mapping
        AccountResponseDTO response = new AccountResponseDTO();
        response.setAccountNumber(acc.getAccountNumber());
        response.setHolderName(acc.getHolderName());
        response.setId(acc.getId());
        response.setStatus(acc.getStatus());
        response.setBalance(acc.getBalance());
        response.setCreatedAt(acc.getCreatedAt());

        return response;
    }
}
