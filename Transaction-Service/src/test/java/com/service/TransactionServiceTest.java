package com.service;

import com.document.Transaction;
import com.document.dto.AmountRequestDTO;
import com.document.dto.ApiResponse;
import com.document.dto.TransferRequestDTO;
import com.enums.TransactionStatus;
import com.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootApplication(scanBasePackages = "com")
public class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    TransactionService transactionService;

    @Test
    void testDepositSuccess() {
        AmountRequestDTO dto = new AmountRequestDTO("ACCXX123", 500.0);

        ApiResponse successResponse = new ApiResponse(true, "OK", null);
        ResponseEntity<ApiResponse> responseEntity = new ResponseEntity<>(successResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(ApiResponse.class)
        )).thenReturn(responseEntity);

        Transaction result = transactionService.deposit(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(TransactionStatus.SUCCESS, result.getStatus());

    }

    @Test
    void testWithdrawSuccess() {
        AmountRequestDTO dto = new AmountRequestDTO("ACCXX123", 300.0);

        ApiResponse successResponse = new ApiResponse(true, "OK", null);
        ResponseEntity<ApiResponse> responseEntity = new ResponseEntity<>(successResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(ApiResponse.class)
        )).thenReturn(responseEntity);

        Transaction result = transactionService.withdraw(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(TransactionStatus.SUCCESS, result.getStatus());
    }

    @Test
    void testTransferSuccess() {
        TransferRequestDTO dto = new TransferRequestDTO("ACCXX1", "ACCXX2", 200.0);

        ApiResponse successResponse = new ApiResponse(true, "OK", null);
        ResponseEntity<ApiResponse> responseEntity = new ResponseEntity<>(successResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(ApiResponse.class)
        )).thenReturn(responseEntity);

        Transaction result = transactionService.transfer(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(TransactionStatus.SUCCESS, result.getStatus());
    }

    @Test
    void testGetAllTransactions() {

        Transaction t1 = new Transaction();
        t1.setTransactionId("T1");
        t1.setSourceAccount("ACCXX123");
        t1.setDestinationAccount(null);

        Transaction t2 = new Transaction();
        t2.setTransactionId("T2");
        t2.setSourceAccount(null);
        t2.setDestinationAccount("ACCXX123");

        List<Transaction> fakeList = List.of(t1, t2);

        when(transactionRepository.findBySourceAccountOrDestinationAccount("ACCXX123", "ACCXX123"))
                .thenReturn(fakeList);

        List<Transaction> result = transactionService.getAllTransactions("ACCXX123");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("T1", result.get(0).getTransactionId());
    }


    @Test
    void testDepositFallback() {
        AmountRequestDTO dto = new AmountRequestDTO("ACCXX123", 500.0);

        Transaction result = transactionService.accountServiceFallback(
                dto,
                new RuntimeException("Service Down")
        );

        Assertions.assertNotNull(result);
        Assertions.assertEquals(TransactionStatus.FAILED, result.getStatus());
    }

    @Test
    void testWithdrawFallback() {
        AmountRequestDTO dto = new AmountRequestDTO("ACCXX999", 100.0);

        Transaction result = transactionService.accountServiceFallback(
                dto,
                new RuntimeException("Account API Down")
        );

        Assertions.assertNotNull(result);
        Assertions.assertEquals(TransactionStatus.FAILED, result.getStatus());
    }

    @Test
    void testTransferFallback() {
        TransferRequestDTO dto = new TransferRequestDTO("ACCXX1", "ACCXX2", 350.0);

        Transaction result = transactionService.accountServiceTransferFallback(
                dto,
                new RuntimeException("Service Down")
        );

        Assertions.assertNotNull(result);
        Assertions.assertEquals(TransactionStatus.FAILED, result.getStatus());
    }

    @Test
    void testNotificationFallback() {
        Assertions.assertDoesNotThrow(() -> {
            transactionService.notificationFallback(
                    "ACCXX123",
                    "Notification failed",
                    new RuntimeException("Service Down")
            );
        });
    }



}
