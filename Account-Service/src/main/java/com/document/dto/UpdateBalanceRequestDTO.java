package com.document.dto;


import jakarta.validation.constraints.NotNull;

public class UpdateBalanceRequestDTO {

    @NotNull(message = "Amount is requeried")
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
    public UpdateBalanceRequestDTO() {}

}
