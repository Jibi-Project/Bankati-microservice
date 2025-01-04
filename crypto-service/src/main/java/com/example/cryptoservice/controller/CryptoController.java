package com.example.cryptoservice.controller;

import com.example.cryptoservice.dto.CryptoRequest;
import com.example.cryptoservice.entity.CryptoTransaction;
import com.example.cryptoservice.entity.TransactionType;
import com.example.cryptoservice.repository.CryptoTransactionRepository;
import com.example.cryptoservice.service.CryptoExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crypto")
public class CryptoController {

    private final CryptoExchangeService cryptoExchangeService;
    private final CryptoTransactionRepository transactionRepository;

    @Autowired
    public CryptoController(CryptoExchangeService cryptoExchangeService, CryptoTransactionRepository transactionRepository) {
        this.cryptoExchangeService = cryptoExchangeService;
        this.transactionRepository = transactionRepository;
    }
    @PostMapping("/buy")
    public ResponseEntity<?> buyCrypto(@RequestBody Map<String, Object> request) {
        String crypto = ((String) request.get("crypto")).toLowerCase();
        String fiat = ((String) request.get("fiat")).toLowerCase();
        Double amount = ((Number) request.get("amount")).doubleValue();

        // Fetch exchange rate
        Map<String, Object> exchangeRate = cryptoExchangeService.getExchangeRate(crypto, fiat);
        Double rate = (Double) exchangeRate.get("rate");

        Double cryptoAmount = cryptoExchangeService.calculateCryptoPurchase(amount, rate);

        // Create and save transaction
        CryptoTransaction transaction = new CryptoTransaction();
        transaction.setUserId(1L); // Replace with actual user ID
        transaction.setCryptoName(crypto);
        transaction.setAmount(cryptoAmount);
        transaction.setTransactionType(TransactionType.BUY);
        transaction.setRate(rate);
        transaction.setFiatCurrency(fiat);
        transactionRepository.save(transaction);

        return ResponseEntity.ok(Map.of("cryptoAmount", cryptoAmount, "rate", rate));
    }


    @PostMapping("/sell")
    public ResponseEntity<?> sellCrypto(@RequestBody CryptoRequest request) {
        // Convert crypto and fiat to lowercase
        String crypto = request.getCrypto().toLowerCase();
        String fiat = request.getFiat().toLowerCase();

        // Fetch exchange rate
        Map<String, Object> exchangeRate = cryptoExchangeService.getExchangeRate(crypto, fiat);

        // Validate exchange rate
        if (exchangeRate == null || !exchangeRate.containsKey("rate")) {
            return ResponseEntity.badRequest().body("Invalid crypto or fiat currency.");
        }

        Double rate = (Double) exchangeRate.get("rate");
        Double fiatAmount = cryptoExchangeService.calculateCryptoSale(request.getAmount(), rate);

        // Create and save transaction
        CryptoTransaction transaction = new CryptoTransaction();
        transaction.setUserId(1L); // Replace with actual user ID
        transaction.setCryptoName(crypto);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.SELL);
        transaction.setRate(rate);
        transaction.setFiatCurrency(fiat);
        transactionRepository.save(transaction);

        return ResponseEntity.ok(Map.of("fiatAmount", fiatAmount, "rate", rate));
    }

    @PostMapping("/convert")
    public ResponseEntity<?> convert(@RequestBody CryptoRequest request) {
        // Convert crypto and fiat to lowercase
        String crypto = request.getCrypto().toLowerCase();
        String fiat = request.getFiat().toLowerCase();
        String direction = request.getDirection().toLowerCase();

        // Fetch exchange rate
        Map<String, Object> exchangeRate = cryptoExchangeService.getExchangeRate(crypto, fiat);

        // Validate exchange rate
        if (exchangeRate == null || !exchangeRate.containsKey("rate")) {
            return ResponseEntity.badRequest().body("Invalid crypto or fiat currency.");
        }

        Double rate = (Double) exchangeRate.get("rate");
        Double result;

        // Handle conversion logic based on direction
        if ("crypto-to-fiat".equals(direction)) {
            result = cryptoExchangeService.convertCryptoToFiat(request.getAmount(), rate);
            return ResponseEntity.ok(Map.of("fiatAmount", result, "rate", rate));
        } else if ("fiat-to-crypto".equals(direction)) {
            result = cryptoExchangeService.convertFiatToCrypto(request.getAmount(), rate);
            return ResponseEntity.ok(Map.of("cryptoAmount", result, "rate", rate));
        } else {
            return ResponseEntity.badRequest().body("Invalid direction. Use 'crypto-to-fiat' or 'fiat-to-crypto'.");
        }
    }


    @GetMapping("/transactions")
    public ResponseEntity<List<CryptoTransaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }
    @GetMapping("/test")
    public ResponseEntity<?> testExchangeRate(@RequestParam String crypto, @RequestParam String fiat) {
        Map<String, Object> rate = cryptoExchangeService.getExchangeRate(crypto, fiat);
        return ResponseEntity.ok(rate);
    }

}
