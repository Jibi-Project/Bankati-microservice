package com.example.transactionservice.controller;

import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @PostMapping("/save")
    public ResponseEntity<Transaction> saveTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }

  /*  @PostMapping("/initiate")
    public ResponseEntity<Transaction> initiateTransaction(@RequestBody Transaction transaction) {
        Transaction newTransaction = transactionService.initiateTransaction(
                transaction.getSenderId(), transaction.getReceiverId(), transaction.getAmount(), transaction.getDescription());
        return ResponseEntity.ok(newTransaction);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getTransactionHistory(userId);
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/complete/{transactionId}")
    public ResponseEntity<Transaction> completeTransaction(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.completeTransaction(transactionId);
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/fail/{transactionId}")
    public ResponseEntity<Transaction> failTransaction(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.failTransaction(transactionId);
        return ResponseEntity.ok(transaction);
    }*/
}
