package com.example.cryptoservice.repository;

import com.example.cryptoservice.entity.CryptoTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoTransactionRepository extends JpaRepository<CryptoTransaction, Long> {}
