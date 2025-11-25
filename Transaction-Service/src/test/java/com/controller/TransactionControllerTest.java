package com.controller;

import com.document.Transaction;
import com.document.dto.AmountRequestDTO;
import com.document.dto.ApiResponse;
import com.document.dto.TransferRequestDTO;
import com.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    TransactionService transactionService;

    @InjectMocks
    TransactionController transactionController;

    @Test
    void testDepositSuccess() {

        AmountRequestDTO dto = new AmountRequestDTO("ACCXX1", 500.0);

        Transaction t = new Transaction();
        t.setSourceAccount("ACCXX1");
        t.setAmount(500.0);

        Mockito.when(transactionService.deposit(dto)).thenReturn(t);

        ResponseEntity<ApiResponse<?>> response = transactionController.deposit(dto);

        Assertions.assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testDepositFallback() {

        AmountRequestDTO dto = new AmountRequestDTO("ACCXX1", 500.0);

        Transaction t = new Transaction(); // fallback returns empty transaction

        Mockito.when(transactionService.deposit(dto)).thenReturn(t);

        ResponseEntity<ApiResponse<?>> response = transactionController.deposit(dto);

        Assertions.assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testWithdrawSuccess() {
        AmountRequestDTO dto = new AmountRequestDTO("ACCXX1", 300.0);

        Transaction t = new Transaction();
        t.setDestinationAccount("ACC1");
        t.setAmount(300.0);

        Mockito.when(transactionService.withdraw(dto)).thenReturn(t);

        ResponseEntity<ApiResponse<?>> response = transactionController.withdraw(dto);

        Assertions.assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testWithdrawFallback() {
        AmountRequestDTO dto = new AmountRequestDTO("ACCXX1", 300.0);

        Mockito.when(transactionService.withdraw(dto)).thenReturn(new Transaction());

        ResponseEntity<ApiResponse<?>> response = transactionController.withdraw(dto);

        Assertions.assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testTransferSuccess() {
        TransferRequestDTO dto = new TransferRequestDTO("ACCXX1", "ACCXX2", 200.0);

        Transaction t = new Transaction();
        t.setSourceAccount("ACCXX1");
        t.setDestinationAccount("ACCXX2");

        Mockito.when(transactionService.transfer(dto)).thenReturn(t);

        ResponseEntity<ApiResponse<?>> response = transactionController.transfer(dto);

        Assertions.assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testTransferFallback() {
        TransferRequestDTO dto = new TransferRequestDTO("ACCXX1", "ACCXX2", 200.0);

        Mockito.when(transactionService.transfer(dto)).thenReturn(new Transaction());

        ResponseEntity<ApiResponse<?>> response = transactionController.transfer(dto);

        Assertions.assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testGetAllTransactions() {

        Transaction t1 = new Transaction();
        t1.setAmount(100.0);

        Transaction t2 = new Transaction();
        t2.setAmount(200.0);

        List<Transaction> list = Arrays.asList(t1, t2);

        Mockito.when(transactionService.getAllTransactions("ACCXX1")).thenReturn(list);

        ResponseEntity<ApiResponse<?>> response = transactionController.getAllTransactions("ACCXX1");

        Assertions.assertTrue(response.getBody().isSuccess());
        Assertions.assertEquals(2, ((List<?>) response.getBody().getData()).size());
    }
}
