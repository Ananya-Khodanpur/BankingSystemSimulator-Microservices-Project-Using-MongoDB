package com.controller;

import com.document.dto.AccountRequestDTO;
import com.document.dto.AccountResponseDTO;
import com.document.dto.ApiResponse;
import com.document.dto.UpdateBalanceRequestDTO;
import com.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Mock
    AccountService accountService;

    @InjectMocks
    AccountController accountController;

    @Test
    void testCreateAccount() {
        AccountRequestDTO req = new AccountRequestDTO();
        req.setHolderName("Ananya");

        AccountResponseDTO fakeResponse = new AccountResponseDTO();
        fakeResponse.setHolderName("Ananya");
        fakeResponse.setAccountNumber("ACCXXAIK123");

        Mockito.when(accountService.createAccount(req)).thenReturn(fakeResponse);

        ResponseEntity<ApiResponse<?>> response = accountController.createAccount(req);

        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().isSuccess());
        Assertions.assertNotNull(response.getBody().getMessage());
        Assertions.assertNotNull(response.getBody().getData());
    }

    @Test
    void testUpdateBalance(){
        UpdateBalanceRequestDTO dto = new UpdateBalanceRequestDTO(200.0);
        AccountResponseDTO fake = new AccountResponseDTO();
        fake.setAccountNumber("ACCXXAIK123");
        fake.setBalance(200.0);

        Mockito.when(accountService.updateBalance("ACCXXAIK123",dto)).thenReturn(fake);

        ResponseEntity<ApiResponse<?>> response =
                accountController.updateBalance("ACCXXAIK123", dto);

        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().isSuccess());
        Assertions.assertNotNull(response.getBody().getMessage());
        Assertions.assertNotNull(response.getBody().getData());
    }

    @Test
    public void testGetAccountMock() {

        AccountResponseDTO fake = new AccountResponseDTO();
        fake.setHolderName("Ananya");
        fake.setAccountNumber("ACCXXAIK123");

        Mockito.when(accountService.getAccountDetails("ACCXXAIK123"))
                .thenReturn(fake);

        ResponseEntity<ApiResponse<?>> response =
                accountController.getAccount("ACCXXAIK123");

        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().isSuccess());
        Assertions.assertNotNull(response.getBody().getMessage());
        Assertions.assertNotNull(response.getBody().getData());
    }

    @Test
    public void testChangeStatusMock() {

        AccountResponseDTO fake = new AccountResponseDTO();
        fake.setAccountNumber("ACCXXAIK123");

        Mockito.when(accountService.changeStatus("ACCXXAIK123"))
                .thenReturn(fake);

        ResponseEntity<ApiResponse<?>> response =
                accountController.activateDeactivateAccount("ACCXXAIK123");

        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().isSuccess());
        Assertions.assertNotNull(response.getBody().getMessage());
        Assertions.assertNotNull(response.getBody().getData());

    }

}







