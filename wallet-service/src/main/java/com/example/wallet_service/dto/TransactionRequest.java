package com.example.wallet_service.dto;


public class TransactionRequest {
    private Long userId;
    private Double amount;
    private String transactionType;// "DEBIT" or "CREDIT"

    public Long getUserId() {
        return userId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }
}
