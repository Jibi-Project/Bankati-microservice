package com.example.cryptoservice.service;

import com.example.cryptoservice.dto.BuyCryptoRequest;
import com.example.cryptoservice.dto.ConvertCryptoRequest;
import com.example.cryptoservice.dto.SellCryptoRequest;
import com.example.cryptoservice.entity.CryptoTransaction;
import com.example.cryptoservice.entity.TransactionType;
import com.example.cryptoservice.repository.CryptoTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CryptoService {

    private final CryptoExchangeService cryptoExchangeService;
    private final CryptoTransactionRepository transactionRepository;



    @Autowired
    public CryptoService(CryptoExchangeService cryptoExchangeService, CryptoTransactionRepository transactionRepository) {
        this.cryptoExchangeService = cryptoExchangeService;
        this.transactionRepository = transactionRepository;
    }

    public Map<String, Object> buyCrypto(Long userId, BuyCryptoRequest request) {
        Map<String, Object> exchangeRate = cryptoExchangeService.getExchangeRate(request.getCrypto(), request.getFiat());
        Double rate = (Double) ((Map<String, Object>) exchangeRate.get(request.getCrypto())).get(request.getFiat());
        Double cryptoAmount = cryptoExchangeService.calculateCryptoPurchase(request.getAmount(), rate);

        // Create and save transaction with the provided userId
        CryptoTransaction transaction = new CryptoTransaction();
        transaction.setUserId(userId); // Set the dynamic userId
        transaction.setCryptoName(request.getCrypto());
        transaction.setAmount(cryptoAmount);
        transaction.setTransactionType(TransactionType.BUY);
        transaction.setRate(rate);
        transaction.setFiatCurrency(request.getFiat());
        transactionRepository.save(transaction);

        return Map.of("cryptoAmount", cryptoAmount, "rate", rate);
    }


    public Map<String, Object> sellCrypto(SellCryptoRequest request) {
        Map<String, Object> exchangeRate = cryptoExchangeService.getExchangeRate(request.getCrypto(), request.getFiat());
        Double rate = (Double) ((Map<String, Object>) exchangeRate.get(request.getCrypto())).get(request.getFiat());
        Double fiatAmount = cryptoExchangeService.calculateCryptoSale(request.getAmount(), rate);

        CryptoTransaction transaction = new CryptoTransaction();
        transaction.setUserId(1L);
        transaction.setCryptoName(request.getCrypto());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.SELL);
        transaction.setRate(rate);
        transaction.setFiatCurrency(request.getFiat());
        transactionRepository.save(transaction);

        return Map.of("fiatAmount", fiatAmount, "rate", rate);
    }

    public Map<String, Object> convertCrypto(ConvertCryptoRequest request) {
        Map<String, Object> exchangeRate = cryptoExchangeService.getExchangeRate(request.getCrypto(), request.getFiat());
        Double rate = (Double) ((Map<String, Object>) exchangeRate.get(request.getCrypto())).get(request.getFiat());
        Double result;

        if ("crypto-to-fiat".equalsIgnoreCase(request.getDirection())) {
            result = cryptoExchangeService.convertCryptoToFiat(request.getAmount(), rate);
            return Map.of("fiatAmount", result, "rate", rate);
        } else if ("fiat-to-crypto".equalsIgnoreCase(request.getDirection())) {
            result = cryptoExchangeService.convertFiatToCrypto(request.getAmount(), rate);
            return Map.of("cryptoAmount", result, "rate", rate);
        } else {
            throw new IllegalArgumentException("Invalid direction. Use 'crypto-to-fiat' or 'fiat-to-crypto'.");
        }
    }

    public List<CryptoTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }


    public List<CryptoTransaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

}
