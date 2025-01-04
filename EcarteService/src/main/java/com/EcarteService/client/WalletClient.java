package com.EcarteService.client;

import com.EcarteService.model.BalanceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "WALLET-SERVICE", url = "http://localhost:1012/api/wallets")
public interface WalletClient {
    @GetMapping("/wallet/{id}")
    BalanceResponse getwalletById(@PathVariable("id") Long id);

    @PutMapping("/wallet/{id}")
    void updateWalletBalance(@PathVariable("id") Long id,  @RequestBody Map<String, Double> request);

    @GetMapping("/{userId}")
    BalanceResponse getBalanceByUserId(@PathVariable("userId") Integer userId);


}

