package com.controller;

import com.document.dto.AccountRequestDTO;
import com.document.dto.AccountResponseDTO;
import com.document.dto.ApiResponse;
import com.document.dto.UpdateBalanceRequestDTO;
import com.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    // Create account
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> createAccount(@Valid @RequestBody AccountRequestDTO dto) {
        AccountResponseDTO accountResponseDTO = accountService.createAccount(dto);
        return ResponseEntity.ok().body(
                ApiResponse.success("Account created successfully", accountResponseDTO)
        );
    }

    // Update balance
    @PutMapping("{accountNumber}/balance")
    public ResponseEntity<ApiResponse<?>> updateBalance(
            @PathVariable("accountNumber") String accountNumber,
            @Valid @RequestBody UpdateBalanceRequestDTO dto
    ) {
        AccountResponseDTO response = accountService.updateBalance(accountNumber, dto);
        return ResponseEntity.ok().body(
                ApiResponse.success("Successfully updated account balance", response)
        );
    }

    // Activate / deactivate account
    @PutMapping(value = "{accountNumber}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> activateDeactivateAccount(
            @PathVariable("accountNumber") String accountNumber
    ) {
        AccountResponseDTO response = accountService.changeStatus(accountNumber);
        return ResponseEntity.ok().body(
                ApiResponse.success("Successfully changed account status", response)
        );
    }

    // Get account details
    @GetMapping(value = "{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> getAccount(@PathVariable("accountNumber") String accountNumber) {
        AccountResponseDTO response = accountService.getAccountDetails(accountNumber);
        return ResponseEntity.ok().body(
                ApiResponse.success("Account fetched successfully", response)
        );
    }
}
