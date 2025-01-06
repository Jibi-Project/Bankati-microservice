package com.EcarteService.controller;

import com.EcarteService.model.RecurringPayment;
import com.EcarteService.service.RecurringPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")

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
    @GetMapping("/all")
    public ResponseEntity<List<RecurringPayment>> getAllRecurringPayments() {
        try {
            List<RecurringPayment> payments = recurringPaymentService.getAllRecurringPayments();
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRecurringPayment(@PathVariable Long id) {
        try {
            recurringPaymentService.deleteRecurringPayment(id);
            return ResponseEntity.ok("Recurring payment deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
