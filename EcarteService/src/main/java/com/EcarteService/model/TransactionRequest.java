package com.EcarteService.model;

public class TransactionRequest {
    private String senderNumeroCarte;
    private String receiverNumeroCarte;
    private Double amount;
    private String description;

    public String getSenderNumeroCarte() {
        return senderNumeroCarte;
    }

    public void setSenderNumeroCarte(String senderNumeroCarte) {
        this.senderNumeroCarte = senderNumeroCarte;
    }

    public String getReceiverNumeroCarte() {
        return receiverNumeroCarte;
    }

    public void setReceiverNumeroCarte(String receiverNumeroCarte) {
        this.receiverNumeroCarte = receiverNumeroCarte;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
// Constructors, getters, and setters
}

