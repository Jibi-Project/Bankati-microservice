package com.example.cryptoservice.controller;

import com.example.cryptoservice.dto.CryptoRequest;
import com.example.cryptoservice.entity.CryptoTransaction;
import com.example.cryptoservice.entity.TransactionType;
import com.example.cryptoservice.repository.CryptoTransactionRepository;
import com.example.cryptoservice.service.CryptoExchangeService;
import com.example.cryptoservice.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/crypto")
public class CryptoController {

    private final CryptoExchangeService cryptoExchangeService;
    private final CryptoService transactionService;

    private final CryptoTransactionRepository transactionRepository;

    @Autowired
    public CryptoController(CryptoExchangeService cryptoExchangeService, CryptoService transactionService,
    CryptoTransactionRepository transactionRepository) {
        this.cryptoExchangeService = cryptoExchangeService;
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CryptoTransaction>> getTransactionsByUserId(@PathVariable Long userId) {
        List<CryptoTransaction> transactions = transactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }
    @PostMapping("/buy")
    public ResponseEntity<?> buyCrypto(@RequestBody Map<String, Object> request) {
        // Vérification des paramètres requis dans la requête
        String crypto = (String) request.get("crypto");
        String fiat = (String) request.get("fiat");
        Number amountObj = (Number) request.get("amount");
        Number userIdObj = (Number) request.get("userId");

        if (crypto == null || fiat == null || amountObj == null || userIdObj == null) {
            return ResponseEntity.badRequest().body("Missing required parameters: crypto, fiat, amount, or userId");
        }

        // Traitement des données
        crypto = crypto.toLowerCase();
        fiat = fiat.toLowerCase();
        Double amount = amountObj.doubleValue();
        Long userId = userIdObj.longValue(); // Extraire le userId

        // Récupérer le taux de change
        Map<String, Object> exchangeRate = cryptoExchangeService.getExchangeRate(crypto, fiat);
        if (exchangeRate == null || !exchangeRate.containsKey("rate")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exchange rate not found for crypto: " + crypto + " and fiat: " + fiat);
        }
        Double rate = (Double) exchangeRate.get("rate");

        // Calculer le montant en crypto
        Double cryptoAmount = cryptoExchangeService.calculateCryptoPurchase(amount, rate);

        // Créer et enregistrer la transaction
        CryptoTransaction transaction = new CryptoTransaction();
        transaction.setUserId(userId);
        transaction.setCryptoName(crypto);
        transaction.setAmount(cryptoAmount);
        transaction.setTransactionType(TransactionType.BUY);
        transaction.setRate(rate);
        transaction.setFiatCurrency(fiat);
        transactionRepository.save(transaction);

        return ResponseEntity.ok(Map.of("cryptoAmount", cryptoAmount, "rate", rate));
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sellCrypto(@RequestBody Map<String, Object> request) {
        // Extract parameters from the request
        String crypto = (String) request.get("crypto");
        String fiat = (String) request.get("fiat");
        Number amountObj = (Number) request.get("amount");
        Number userIdObj = (Number) request.get("userId");

        // Validate required parameters
        if (crypto == null || fiat == null || amountObj == null || userIdObj == null) {
            return ResponseEntity.badRequest().body("Missing required parameters: crypto, fiat, amount, or userId");
        }

        // Process input
        crypto = crypto.toLowerCase();
        fiat = fiat.toLowerCase();
        Double amount = amountObj.doubleValue();
        Long userId = userIdObj.longValue(); // Extract userId

        // Fetch exchange rate
        Map<String, Object> exchangeRate = cryptoExchangeService.getExchangeRate(crypto, fiat);

        // Validate exchange rate
        if (exchangeRate == null || !exchangeRate.containsKey("rate")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exchange rate not found for crypto: " + crypto + " and fiat: " + fiat);
        }
        Double rate = (Double) exchangeRate.get("rate");

        // Calculate fiat amount for sale
        Double fiatAmount = cryptoExchangeService.calculateCryptoSale(amount, rate);

        // Create and save transaction
        CryptoTransaction transaction = new CryptoTransaction();
        transaction.setUserId(userId); // Use provided userId
        transaction.setCryptoName(crypto);
        transaction.setAmount(amount);
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
