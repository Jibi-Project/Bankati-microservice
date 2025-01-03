package com.EcarteService.repository;

import com.EcarteService.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderId(String senderId);
    List<Transaction> findByReceiverId(String receiverId);
}
