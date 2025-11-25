package com.dto;


import jakarta.validation.constraints.NotBlank;

public class NotificationRequestDTO {
    @NotBlank(message = "account number should not be empty")
    private String accountNumber;
    @NotBlank(message = "message should not be empty")
    private String message;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationRequestDTO(String accountNumber, String message) {
        this.accountNumber = accountNumber;
        this.message = message;
    }

    public NotificationRequestDTO() {}
}
