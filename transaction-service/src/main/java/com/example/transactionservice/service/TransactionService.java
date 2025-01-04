package com.example.transactionservice.service;

import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EmailService emailService;


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
        // Retrieve and update transaction
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transaction.setStatus("COMPLETED");
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Fetch user email dynamically
        String userEmail = fetchUserEmail(transaction.getSenderId());

        // Send email notification
        String emailBody = "Your transaction of amount $" + transaction.getAmount() + " was successfully completed.";
        emailService.sendEmail(userEmail, "Transaction Confirmation", emailBody);

        return transaction;
    }

    public Transaction failTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transaction.setStatus("FAILED");
        transaction.setUpdatedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }
    // Method to fetch user email from User Service
    private String fetchUserEmail(Long userId) {
        // Define the endpoint of the User Service
        String userServiceUrl = "http://localhost:8080/users/email-by-id/" + userId;

        try {
            return restTemplate.getForObject(userServiceUrl, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user email for userId: " + userId, e);
        }
    }

}
