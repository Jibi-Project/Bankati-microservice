package com.example.transactionservice.service;

import com.example.transactionservice.model.RecurringPayment;
import com.example.transactionservice.repository.RecurringPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecurringPaymentService {

    @Autowired
    private RecurringPaymentRepository recurringPaymentRepository;

    @Autowired
    private EmailService emailService;

    public void processRecurringPayments() {
        // Find due payments
        List<RecurringPayment> duePayments = recurringPaymentRepository.findByNextExecutionBeforeAndStatus(LocalDateTime.now(), "ACTIVE");

        for (RecurringPayment payment : duePayments) {
            try {
                // Notify user about upcoming payment
                emailService.sendRecurringPaymentNotification(
                        "user" + payment.getUserId() + "@example.com", // Placeholder email
                        payment.getAmount(),
                        payment.getDescription(),
                        payment.getNextExecution().toString()
                );

                // Simulate payment processing
                System.out.println("Processing recurring payment for user: " + payment.getUserId());

                // Send payment confirmation
                emailService.sendPaymentConfirmation(
                        "user" + payment.getUserId() + "@example.com", // Placeholder email
                        payment.getAmount(),
                        payment.getDescription()
                );

                // Update the next execution time
                payment.setNextExecution(calculateNextExecution(payment.getFrequency()));
                recurringPaymentRepository.save(payment);

            } catch (Exception e) {
                System.err.println("Failed to process recurring payment for user: " + payment.getUserId());
                e.printStackTrace();
            }
        }
    }

    private LocalDateTime calculateNextExecution(String frequency) {
        switch (frequency) {
            case "DAILY":
                return LocalDateTime.now().plusDays(1);
            case "WEEKLY":
                return LocalDateTime.now().plusWeeks(1);
            case "MONTHLY":
                return LocalDateTime.now().plusMonths(1);
            default:
                throw new IllegalArgumentException("Invalid frequency: " + frequency);
        }
    }
}
