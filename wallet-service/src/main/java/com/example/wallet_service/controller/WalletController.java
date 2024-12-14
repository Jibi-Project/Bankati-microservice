package com.example.wallet_service.controller;

import com.example.wallet_service.dto.TransactionRequest;
import com.example.wallet_service.model.Wallet;
import com.example.wallet_service.service.WalletService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping("/{userId}")
    public Wallet getWallet(@PathVariable Long userId) {
        return walletService.getWalletByUserId(userId);
    }

    @PostMapping("/transaction")
    public Wallet handleTransaction(@RequestBody TransactionRequest request) {
        return walletService.updateWalletBalance(request.getUserId(), request.getAmount(), request.getTransactionType());
    }
}

