package com.EcarteService.service;


import com.EcarteService.client.UserClient;
import com.EcarteService.client.WalletClient;
import com.EcarteService.model.BalanceResponse;
import com.EcarteService.model.ECarte;
import com.EcarteService.model.User;
import com.EcarteService.repository.ECarteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ECarteService {

    @Autowired
    private ECarteRepository eCarteRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private WalletClient walletClient;

    public ECarte genererECarte(String email) {
        Optional<ECarte> existingECarte = eCarteRepository.findByEmailUtilisateur(email);
        if (existingECarte.isPresent()) {
            return existingECarte.get();
        }

        User utilisateur = userClient.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Retrieve wallet balance
        BalanceResponse balanceResponse = walletClient.getBalanceByUserId(utilisateur.getId());
        Double balance = balanceResponse.getBalance();

        ECarte eCarte = new ECarte();
        eCarte.setNumeroCarte(genererNumeroCarte());
        eCarte.setDateExpiration(LocalDate.now().plusYears(1));
        eCarte.setCvv(genererCvv());
        eCarte.setEmailUtilisateur(utilisateur.getEmail());
        eCarte.setNomClient(utilisateur.getNom());
        eCarte.setBalance(balance); // Set the balance from Wallet service

        return eCarteRepository.save(eCarte);
    }

    private String genererNumeroCarte() {
        return "4000-" + (int) (Math.random() * 10000) + "-" + (int) (Math.random() * 10000) + "-" + (int) (Math.random() * 10000);
    }

    private String genererCvv() {
        return String.valueOf((int) (Math.random() * 900) + 100);
    }
}
