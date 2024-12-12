package com.example.transactionservice.service;

import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction initiateTransaction(Long senderId, Long receiverId, Double amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setSenderId(senderId);
        transaction.setReceiverId(receiverId);
        transaction.setAmount(amount);
        transaction.setStatus("PENDING");
        transaction.setDescription(description);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionHistory(Long userId) {
        return transactionRepository.findBySenderId(userId);
    }

    public Transaction completeTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transaction.setStatus("COMPLETED");
        transaction.setUpdatedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public Transaction failTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transaction.setStatus("FAILED");
        transaction.setUpdatedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }
}
