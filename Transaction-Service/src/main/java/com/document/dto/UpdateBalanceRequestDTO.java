package com.document.dto;


public class UpdateBalanceRequestDTO {
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public UpdateBalanceRequestDTO(Double amount) {
        this.amount = amount;
    }

    public UpdateBalanceRequestDTO() {
    }
}
