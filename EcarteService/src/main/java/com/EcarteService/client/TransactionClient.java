package com.EcarteService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "TRANSACTION-SERVICE", url = "http://localhost:1020/api/transactions")
public interface TransactionClient {

    @PostMapping("/initiate")
    Transaction initiateTransaction(@RequestBody TransactionRequest request);
}

