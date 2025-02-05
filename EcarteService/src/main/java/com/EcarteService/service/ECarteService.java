package com.EcarteService.service;

import com.EcarteService.client.UserClient;
import com.EcarteService.client.WalletClient;
import com.EcarteService.model.BalanceResponse;
import com.EcarteService.model.ECarte;
import com.EcarteService.model.Transaction;
import com.EcarteService.model.User;
import com.EcarteService.repository.ECarteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ECarteService {

    @Autowired
    private ECarteRepository eCarteRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private WalletClient walletClient;

    @Autowired
    private EmailService emailService;

    public ECarte genererECarte(String email) {
        Optional<ECarte> existingECarte = eCarteRepository.findByEmailUtilisateur(email);
        if (existingECarte.isPresent()) {
            return existingECarte.get();
        }

        User utilisateur = userClient.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Retrieve wallet balance
        BalanceResponse balanceResponse = walletClient.getBalanceByUserId(utilisateur.getId());
        Long walletId = balanceResponse.getId();

        ECarte eCarte = new ECarte();
        eCarte.setNumeroCarte(genererNumeroCarte());
        eCarte.setDateExpiration(LocalDate.now().plusYears(1));
        eCarte.setCvv(genererCvv());
        eCarte.setEmailUtilisateur(utilisateur.getEmail());
        eCarte.setNomClient(utilisateur.getNom());
        eCarte.setWalletId(walletId);

        return eCarteRepository.save(eCarte);
    }

    private String genererNumeroCarte() {
        return "4000"
                + (int) (Math.random() * 10000)
                + (int) (Math.random() * 10000)
                + (int) (Math.random() * 10000);
    }

    private String genererCvv() {
        return String.valueOf((int) (Math.random() * 900) + 100);
    }

    public ECarte getECarteByEmail(String email) {
        return eCarteRepository.findByEmailUtilisateur(email)
                .orElseThrow(() -> new RuntimeException("ECarte not found for the provided email: " + email));
    }

    @Transactional
    public String doTransaction(String senderNumeroCarte, String receiverNumeroCarte, Double amount, String description) {
        // Retrieve sender and receiver eCartes
        ECarte sender = eCarteRepository.findByNumeroCarte(senderNumeroCarte)
                .orElseThrow(() -> new RuntimeException("Sender card not found"));

        ECarte receiver = eCarteRepository.findByNumeroCarte(receiverNumeroCarte)
                .orElseThrow(() -> new RuntimeException("Receiver card not found"));

        // Fetch balances from Wallet service
        BalanceResponse senderWallet = walletClient.getwalletById(sender.getWalletId());
        BalanceResponse receiverWallet = walletClient.getwalletById(receiver.getWalletId());

        // Validate sender's balance
        if (senderWallet.getBalance() == null || senderWallet.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance on sender's wallet");
        }

        // Update sender and receiver balances via Wallet service
        Map<String, Double> request = new HashMap<>();
        request.put("newBalance", senderWallet.getBalance() - amount);
        walletClient.updateWalletBalance(sender.getWalletId(), request);

        request.put("newBalance", receiverWallet.getBalance() + amount);
        walletClient.updateWalletBalance(receiver.getWalletId(), request);

        // Save transaction
        Transaction transaction = new Transaction();
        transaction.setSenderId(senderNumeroCarte);
        transaction.setReceiverId(receiverNumeroCarte);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transactionService.saveTransaction(transaction);

        // Send email notifications
        sendTransactionEmails(sender, receiver, amount, description);

        // Return a success message
        return "Transaction of " + amount + " from " + senderNumeroCarte + " to " + receiverNumeroCarte + " was successful.";
    }

    private void sendTransactionEmails(ECarte sender, ECarte receiver, Double amount, String description) {
        // Email for sender
        emailService.sendTransactionCompletionEmail(
                sender.getEmailUtilisateur(),
                amount,
                sender.getNumeroCarte(),
                receiver.getNumeroCarte(),
                description
        );

        // Email for receiver
        emailService.sendTransactionCompletionEmail(
                receiver.getEmailUtilisateur(),
                amount,
                sender.getNumeroCarte(),
                receiver.getNumeroCarte(),
                description
        );
    }
    public ECarte getECarteByUserId(Long userId) {
        return eCarteRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("ECarte not found for user ID: " + userId));
    }


    public ECarte getECarteByServiceName(String serviceName) {
        return eCarteRepository.findByEmailUtilisateur(serviceName)
                .orElseThrow(() -> new RuntimeException("ECarte not found for service: " + serviceName));
    }



}
