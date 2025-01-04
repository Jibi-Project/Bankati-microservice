package com.example.transactionservice.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class RecurringPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Double amount;
    private String description;
    private String frequency; // e.g., "DAILY", "WEEKLY", "MONTHLY"
    private LocalDateTime nextExecution;
    private String status; // e.g., "ACTIVE", "CANCELLED"

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public LocalDateTime getNextExecution() {
        return nextExecution;
    }

    public void setNextExecution(LocalDateTime nextExecution) {
        this.nextExecution = nextExecution;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
