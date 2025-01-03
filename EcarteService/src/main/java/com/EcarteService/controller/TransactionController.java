package com.EcarteService.controller;


import com.EcarteService.model.Transaction;
import com.EcarteService.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
