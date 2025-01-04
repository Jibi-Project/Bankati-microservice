package com.example.wallet_service.dto;

public class UserWalletResponse {
    private String username;
    private Double balance;

    // Constructors, getters, and setters
    public UserWalletResponse(String username, Double balance) {
        this.username = username;
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
