package com.EcarteService.repository;

import com.EcarteService.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderId(String senderId);

    @Query("SELECT AVG(t.amount) FROM Transaction t")
    Double findAverageTransactionAmount();

    List<Transaction> findByReceiverId(String receiverId);
}
