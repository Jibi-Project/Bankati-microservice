package com.EcarteService.client;

import com.EcarteService.model.BalanceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "WALLET-SERVICE", url = "http://localhost:1012/api/wallets")
public interface WalletClient {

    @GetMapping("/{userId}")
    BalanceResponse getBalanceByUserId(@PathVariable("userId") Integer userId);
}

