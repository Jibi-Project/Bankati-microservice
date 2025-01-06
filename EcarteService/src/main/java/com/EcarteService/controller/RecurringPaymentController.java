package com.EcarteService.controller;

import com.EcarteService.service.RecurringPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/recurring-payment")
public class RecurringPaymentController {

    @Autowired
    private RecurringPaymentService recurringPaymentService;

    @PostMapping("/add")
    public ResponseEntity<String> addRecurringPayment(@RequestBody Map<String, Object> payload) {
        Long senderUserId = Long.valueOf(payload.get("senderUserId").toString());
        String serviceName = (String) payload.get("serviceName");
        Double amount = Double.valueOf(payload.get("amount").toString());
        String frequency = (String) payload.get("frequency");
        LocalDate startDate = LocalDate.parse(payload.get("startDate").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = payload.get("endDate") != null
                ? LocalDate.parse(payload.get("endDate").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : null;

        try {
            String result = recurringPaymentService.addRecurringPayment(senderUserId, serviceName, amount, frequency, startDate, endDate);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
