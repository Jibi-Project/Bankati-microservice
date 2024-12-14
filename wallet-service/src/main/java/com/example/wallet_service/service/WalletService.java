package com.example.wallet_service.service;

import com.example.wallet_service.model.Wallet;
import com.example.wallet_service.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    @Transactional
    public Wallet updateWalletBalance(Long userId, Double amount, String transactionType) {
        Wallet wallet = getWalletByUserId(userId);
        if ("DEBIT".equalsIgnoreCase(transactionType) && wallet.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }
        double newBalance = "DEBIT".equalsIgnoreCase(transactionType)
                ? wallet.getBalance() - amount
                : wallet.getBalance() + amount;
        wallet.setBalance(newBalance);
        return walletRepository.save(wallet);
    }
}

