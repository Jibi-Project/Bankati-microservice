package com.example.wallet_service.service;

import com.example.wallet_service.client.UserClient;
import com.example.wallet_service.dto.UserWalletResponse;
import com.example.wallet_service.model.User;
import com.example.wallet_service.model.Wallet;
import com.example.wallet_service.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final UserClient userClient;

    @Autowired
    public WalletService(WalletRepository walletRepository, UserClient userClient) {
        this.walletRepository = walletRepository;
        this.userClient = userClient;
    }

    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public Wallet getWalletById(Long Id) {
        return walletRepository.findById(Id)
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

    public Wallet createWalletForUserByEmail(String email, Double initialBalance) {
        User user = userClient.findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        Long userId = user.getId().longValue();
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(initialBalance);
        return walletRepository.save(wallet);
    }


    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }


}
