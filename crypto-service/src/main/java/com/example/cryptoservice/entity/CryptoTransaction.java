package com.example.cryptoservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class CryptoTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @NotNull(message = "User ID is required")
    @JsonProperty("userId")
    private Long userId;

    @NotNull(message = "Crypto name is required")
    @JsonProperty("cryptoName")
    private String cryptoName;

    @Positive(message = "Amount must be a positive number")
    @JsonProperty("amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Transaction type is required")
    @JsonProperty("transactionType")
    private TransactionType transactionType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("transactionDate")
    private LocalDateTime transactionDate = LocalDateTime.now();

    @Positive(message = "Rate must be a positive number")
    @JsonProperty("rate")
    private Double rate; // Exchange rate used for the transaction

    @NotNull(message = "Fiat currency is required")
    @JsonProperty("fiatCurrency")
    private String fiatCurrency; // Currency used for the transaction

    @JsonProperty("status")
    private String status = "PENDING"; // Status of the transaction (e.g., PENDING, COMPLETED)

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

    public String getCryptoName() {
        return cryptoName;
    }

    public void setCryptoName(String cryptoName) {
        this.cryptoName = cryptoName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getFiatCurrency() {
        return fiatCurrency;
    }

    public void setFiatCurrency(String fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
