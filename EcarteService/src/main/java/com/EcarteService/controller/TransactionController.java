package com.EcarteService.controller;


import com.EcarteService.model.Transaction;
import com.EcarteService.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @GetMapping("alltrans")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
    @GetMapping("/by-sender")
    public List<Transaction> getTransactionsBySenderId(@RequestParam String senderId) {
        return transactionService.getTransactionsBySenderId(senderId);
    }

    @GetMapping("/per-day")
    public Map<LocalDate, Long> getTransactionsPerDay() {
        return transactionService.getTransactionsPerDay();
    }

    @GetMapping("/average-amount")
    public Double getAverageTransactionAmount() {
        return transactionService.getAverageTransactionAmount();
    }


}
