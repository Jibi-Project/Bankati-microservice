package com.EcarteService.service;

import com.EcarteService.model.Transaction;
import com.EcarteService.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsBySenderId(String senderId) {
        return transactionRepository.findBySenderId(senderId);
    }

    public Map<LocalDate, Long> getTransactionsPerDay() {
        return transactionRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCreatedAt().toLocalDate(),
                        Collectors.counting()
                ));
    }

    public Double getAverageTransactionAmount() {
        return transactionRepository.findAverageTransactionAmount();
    }

}
