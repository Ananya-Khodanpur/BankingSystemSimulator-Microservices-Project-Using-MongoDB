package com.document.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AmountRequestDTO {
    @NotBlank(message = "Account number is required")
    private String accountNumber;
    @NotNull(message = "Amount is required")
    private Double amount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public AmountRequestDTO(String accountNumber, Double amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public AmountRequestDTO() {}
}
