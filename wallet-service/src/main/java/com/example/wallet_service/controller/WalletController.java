package com.example.wallet_service.controller;

import com.example.wallet_service.dto.TransactionRequest;
import com.example.wallet_service.dto.UserWalletResponse;
import com.example.wallet_service.model.Wallet;
import com.example.wallet_service.repository.WalletRepository;
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

    @Autowired
    private WalletRepository walletRepository;

    @GetMapping("/wallet/{id}")
    public Wallet getWalletById(@PathVariable Long id) {
        return walletService.getWalletById(id);
    }


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

    @PutMapping("/wallet/{id}")
    public ResponseEntity<?> updateWalletBalance(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Double newBalance = request.get("newBalance"); // Get newBalance from the request body
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
        return ResponseEntity.ok("Balance updated successfully");
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

    @PutMapping("/{userId}/updateBalance")
    public ResponseEntity<String> updateBalance(
            @PathVariable Long userId,
            @RequestParam String action,
            @RequestParam Double amount) {
        try {
            Wallet wallet = walletRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Wallet not found for userId: " + userId));

            wallet.updateBalance(action, amount);
            walletRepository.save(wallet);

            return ResponseEntity.ok("Balance updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating balance");
        }
    }

}

