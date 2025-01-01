package com.example.wallet_service.controller;

import com.example.wallet_service.dto.TransactionRequest;
import com.example.wallet_service.dto.UserWalletResponse;
import com.example.wallet_service.model.Wallet;
import com.example.wallet_service.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:4200") // Allow requests from Angular
@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping("/{userId}")
    public Wallet getWallet(@PathVariable Long userId) {
        return walletService.getWalletByUserId(userId);
    }


    // New endpoint for just the balance
    @GetMapping("/balance/{userId}")
    public Double getWalletBalance(@PathVariable Long userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        return wallet != null ? wallet.getBalance() : 0.0; // Return 0.0 if wallet is null
    }
    @PostMapping("/transaction")
    public Wallet handleTransaction(@RequestBody TransactionRequest request) {
        return walletService.updateWalletBalance(request.getUserId(), request.getAmount(), request.getTransactionType());
    }

    @PostMapping("/create")
    public ResponseEntity<Wallet> createWallet(
            @RequestParam("email") String email,
            @RequestParam("initialBalance") Double initialBalance) {
        System.out.println("Create wallet called with email: " + email + ", balance: " + initialBalance);
        try {
            Wallet wallet = walletService.createWalletForUserByEmail(email, initialBalance);
            return new ResponseEntity<>(wallet, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


//    @GetMapping("/details")
//    public ResponseEntity<List<Map<String, Object>>> getAllWalletsWithUserDetails() {
//        List<Map<String, Object>> wallets = walletService.getAllWalletsWithUserDetails();
//        return ResponseEntity.ok(wallets);
//    }

    @GetMapping("/all-wallets")
    public List<Wallet> getAllWallets() {
        return walletService.getAllWallets();
    }

}

