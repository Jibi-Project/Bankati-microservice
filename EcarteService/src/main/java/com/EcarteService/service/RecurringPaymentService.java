package com.EcarteService.service;

import com.EcarteService.model.ECarte;
import com.EcarteService.model.RecurringPayment;
import com.EcarteService.repository.ECarteRepository;
import com.EcarteService.repository.RecurringPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecurringPaymentService {

    @Autowired
    private RecurringPaymentRepository recurringPaymentRepository;

    @Autowired
    private ECarteRepository eCarteRepository;

    @Autowired
    private ECarteService eCarteService;

    @Transactional
    public String addRecurringPayment(Long senderUserId, String serviceName, Double amount, String frequency, LocalDate startDate, LocalDate endDate) {
        // Fetch sender's ECarte using walletId (or another unique identifier for the user)
        ECarte senderECarte = eCarteRepository.findByWalletId(senderUserId)
                .orElseThrow(() -> new RuntimeException("Sender ECarte not found for userId: " + senderUserId));

        String senderNumeroCarte = senderECarte.getNumeroCarte();

        // Fetch receiver's ECarte using serviceName
        ECarte receiverECarte = eCarteRepository.findByNomClient(serviceName)
                .orElseThrow(() -> new RuntimeException("Receiver ECarte not found for serviceName: " + serviceName));

        String receiverNumeroCarte = receiverECarte.getNumeroCarte();

        // Save the recurring payment record
        RecurringPayment recurringPayment = new RecurringPayment();
        recurringPayment.setSenderUserId(senderUserId);
        recurringPayment.setAmount(amount);
        recurringPayment.setServiceName(serviceName);
        recurringPayment.setFrequency(frequency);
        recurringPayment.setStartDate(startDate);
        recurringPayment.setEndDate(endDate);
        recurringPayment.setStatus("Active");

        recurringPaymentRepository.save(recurringPayment);

        // Immediately trigger the first transaction
        String transactionResult = eCarteService.doTransaction(senderNumeroCarte, receiverNumeroCarte, amount, "First recurring payment for " + serviceName);

        return "Recurring payment added and first transaction completed: " + transactionResult;
    }
    public List<RecurringPayment> getAllRecurringPayments() {
        return recurringPaymentRepository.findAll();
    }
    public void deleteRecurringPayment(Long id) {
        if (!recurringPaymentRepository.existsById(id)) {
            throw new RuntimeException("Recurring payment not found with ID: " + id);
        }
        recurringPaymentRepository.deleteById(id);
    }
}
