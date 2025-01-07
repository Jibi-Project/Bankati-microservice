package com.example.wallet_service.model;

import jakarta.persistence.*;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Double balance;

    // Constructor
    public Wallet() {}

    public Wallet(Long id, Long userId, Double balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Double getBalance() {
        return balance;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void updateBalance(String action, Double amount) {
        if (action.equalsIgnoreCase("buy")) {
            if (balance < amount) {
                throw new IllegalArgumentException("Insufficient balance");
            }
            this.balance -= amount;
        } else if (action.equalsIgnoreCase("sell")) {
            this.balance += amount;
        } else {
            throw new IllegalArgumentException("Invalid action. Use 'buy' or 'sell'.");
        }
    }
}
