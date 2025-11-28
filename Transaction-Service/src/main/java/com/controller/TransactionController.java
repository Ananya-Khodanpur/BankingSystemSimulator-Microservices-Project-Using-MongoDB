package com.controller;

import com.document.Transaction;
import com.document.dto.AmountRequestDTO;
import com.document.dto.ApiResponse;
import com.document.dto.TransferRequestDTO;
import com.service.TransactionService;
import jakarta.validation.Valid;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

        Logger logger = LoggerFactory.getLogger(TransactionController.class);
    // Deposit
    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<?>> deposit(@Valid @RequestBody AmountRequestDTO dto){
        logger.info("deposit called");

        Transaction t = transactionService.deposit(dto);

        if (t.getSourceAccount() == null && t.getDestinationAccount() == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.failure("Deposit failed, Account service unavailable", t)
            );
        }

        return ResponseEntity.ok(ApiResponse.success("Deposit completed", t));
    }

    // Withdraw
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<?>> withdraw(@Valid @RequestBody AmountRequestDTO dto) {
        Transaction t = transactionService.withdraw(dto);

        if (t.getSourceAccount() == null && t.getDestinationAccount() == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.failure("Withdraw failed, Account service unavailable", t)
            );
        }

        return ResponseEntity.ok(ApiResponse.success("Withdraw completed", t));
    }

    // Transfer
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<?>> transfer(@Valid @RequestBody TransferRequestDTO dto) {
        Transaction t = transactionService.transfer(dto);

        if (t.getSourceAccount() == null && t.getDestinationAccount() == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.failure("Transfer failed, Account service unavailable", t)
            );
        }

        return ResponseEntity.ok(ApiResponse.success("Transfer completed", t));
    }

    // Get all transactions
    @GetMapping(value = "{accNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> getAllTransactions(@PathVariable("accNo") String accNo) {
        List<Transaction> transactions = transactionService.getAllTransactions(accNo);

        return ResponseEntity.ok()
                .body(ApiResponse.success("Successfully fetched all transactions", transactions));
    }
}
