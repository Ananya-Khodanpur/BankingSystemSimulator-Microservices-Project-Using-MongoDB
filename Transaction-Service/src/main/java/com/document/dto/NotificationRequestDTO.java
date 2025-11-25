package com.document.dto;

public class NotificationRequestDTO {
    private String accountNumber;
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
    public NotificationRequestDTO() {
    }
}
