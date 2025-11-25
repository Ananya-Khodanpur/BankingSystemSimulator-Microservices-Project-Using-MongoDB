package com.document.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class AccountRequestDTO {
    @NotBlank(message = "Holder name is required")
    private String  holderName;

//    @Min(value = 0, message = "Initial balance should not be null")
//    private Double amount;


    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }
}
