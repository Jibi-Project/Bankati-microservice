package com.example.transactionservice.controller;


import com.example.transactionservice.service.RecurringPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recurring-payments")
public class RecurringPaymentController {

    @Autowired
    private RecurringPaymentService recurringPaymentService;

    @PostMapping("/process")
    public String processRecurringPayments() {
        recurringPaymentService.processRecurringPayments();
        return "Recurring payments processed successfully.";
    }
}
