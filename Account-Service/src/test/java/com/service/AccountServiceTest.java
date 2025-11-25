package com.service;

import com.document.Account;
import com.document.dto.AccountRequestDTO;
import com.document.dto.AccountResponseDTO;
import com.document.dto.UpdateBalanceRequestDTO;
import com.enums.AccountStatus;
import com.exceptions.AccountNotFoundException;
import com.exceptions.InvalidAccountStatusException;
import com.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootApplication(scanBasePackages = "com")
public class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountService accountService;


    //test create account
    @Test
    void testCreateAccount() {
        AccountRequestDTO dto = new AccountRequestDTO();
        dto.setHolderName("Ananya");

        Account acc = new Account();
        acc.setAccountNumber("ACCXX123");
        acc.setHolderName("Ananya");
        acc.setBalance(0.0);
        acc.setStatus(AccountStatus.ACTIVE);
        acc.setCreatedAt(LocalDateTime.now());

       when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
       when(accountRepository.save(any(Account.class))).thenReturn(acc);

        AccountResponseDTO res = accountService.createAccount(dto);

        Assertions.assertNotNull(res);
        Assertions.assertEquals("Ananya", res.getHolderName());

        System.out.println("Create account success done");
    }

    @Test
    void testGetAccount(){
        String accNo = "ACCXXAIK001";

        Account acc = new Account();
        acc.setAccountNumber("ACCXX123");
        acc.setHolderName("Ananya");
        acc.setBalance(0.0);
        acc.setStatus(AccountStatus.ACTIVE);
        acc.setCreatedAt(LocalDateTime.now());

        when(accountRepository.findByAccountNumber(accNo)).thenReturn(Optional.of(acc));

        AccountResponseDTO res = accountService.getAccountDetails(accNo);

        Assertions.assertNotNull(res);
        Assertions.assertEquals("Ananya",res.getHolderName());

        System.out.println("Get account details done");
    }

    @Test
    void testGetAccountAccountNotFound(){
        String accNo = "ACCXXAIK001";

        when(accountRepository.findByAccountNumber(accNo)).thenReturn(Optional.empty());
        Assertions.assertThrows(AccountNotFoundException.class,()->{
            accountService.getAccountDetails(accNo);
        });
    }

    @Test
    void testUpdateBalance(){
        UpdateBalanceRequestDTO dto = new UpdateBalanceRequestDTO();
        dto.setAmount(500.0);

        Account acc = new Account();
        acc.setAccountNumber("ACCXX123");
        acc.setHolderName("Ananya");
        acc.setBalance(0.0);
        acc.setStatus(AccountStatus.ACTIVE);
        acc.setCreatedAt(LocalDateTime.now());

        when(accountRepository.findByAccountNumber("ACCXX123")).thenReturn(Optional.of(acc));
        when(accountRepository.save(any(Account.class))).thenReturn(acc);

        AccountResponseDTO res = accountService.updateBalance("ACCXX123",dto);

        Assertions.assertNotNull(res);
        Assertions.assertEquals(500.0,res.getBalance());

        System.out.println("update balance success done");
    }

    @Test
    void testUpdateBalanceAccountNotFound(){
        UpdateBalanceRequestDTO dto = new UpdateBalanceRequestDTO();
        dto.setAmount(500.0);

        when(accountRepository.findByAccountNumber("ACCXXAIK001")).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class,()->{
            accountService.getAccountDetails("ACCXXAIK001");
        });
    }

    @Test
    void testUpdateBalanceInvalidAccStatus(){
        UpdateBalanceRequestDTO dto = new UpdateBalanceRequestDTO();
        dto.setAmount(500.0);

        Account acc = new Account();
        acc.setAccountNumber("ACCXX123");
        acc.setHolderName("Ananya");
        acc.setBalance(0.0);
        acc.setStatus(AccountStatus.INACTIVE);
        acc.setCreatedAt(LocalDateTime.now());

        when(accountRepository.findByAccountNumber("ACCXXAIK001")).thenReturn(Optional.of(acc));

        Assertions.assertThrows(InvalidAccountStatusException.class,()->{
            accountService.updateBalance("ACCXXAIK001",dto);
        });
    }

    @Test
    void testChangeStatus(){
        String accNo = "ACCXXAIK001";

        Account acc = new Account();
        acc.setAccountNumber("ACCXX123");
        acc.setHolderName("Ananya");
        acc.setBalance(0.0);
        acc.setStatus(AccountStatus.INACTIVE);
        acc.setCreatedAt(LocalDateTime.now());

        when(accountRepository.findByAccountNumber(accNo)).thenReturn(Optional.of(acc));
     AccountResponseDTO res = accountService.changeStatus(accNo);
     Assertions.assertNotNull(res);
     Assertions.assertEquals(AccountStatus.ACTIVE,acc.getStatus());

    }

    @Test
    void testChangeStatusAccountNotFound(){
        String accNo = "ACCXXAIK001";

        when(accountRepository.findByAccountNumber(accNo)).thenReturn(Optional.empty());
        Assertions.assertThrows(AccountNotFoundException.class,()->{
            accountService.changeStatus(accNo);
        });
    }



}
