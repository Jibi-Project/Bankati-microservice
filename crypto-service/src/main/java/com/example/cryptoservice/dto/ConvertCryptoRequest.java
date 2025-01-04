package com.example.cryptoservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ConvertCryptoRequest {
    @NotNull(message = "Crypto name is required")
    private String crypto;

    @NotNull(message = "Fiat currency is required")
    private String fiat;

    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Direction is required (crypto-to-fiat or fiat-to-crypto)")
    private String direction;

    public String getCrypto() {
        return crypto;
    }

    public void setCrypto(String crypto) {
        this.crypto = crypto;
    }

    public String getFiat() {
        return fiat;
    }

    public void setFiat(String fiat) {
        this.fiat = fiat;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
