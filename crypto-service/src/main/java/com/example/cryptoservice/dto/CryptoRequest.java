package com.example.cryptoservice.dto;

import lombok.Data;

@Data
public class CryptoRequest {
    private String crypto;
    private String fiat;
    private Double amount;
    private String direction;
    // For convert operation

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
