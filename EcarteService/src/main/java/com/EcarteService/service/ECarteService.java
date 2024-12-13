package com.EcarteService.service;


import com.EcarteService.client.UserClient;
import com.EcarteService.model.ECarte;
import com.EcarteService.model.User;
import com.EcarteService.repository.ECarteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ECarteService {

    @Autowired
    private ECarteRepository eCarteRepository;

    @Autowired
    private UserClient client;

    public ECarte genererECarte(String email) {
        User utilisateur = client.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        ECarte eCarte = new ECarte();
        eCarte.setNumeroCarte(genererNumeroCarte());
        eCarte.setDateExpiration(LocalDate.now().plusYears(1));
        eCarte.setCvv(genererCvv());
        eCarte.setEmailUtilisateur(utilisateur.getEmail());
        eCarte.setNomClient(utilisateur.getNom());

        return eCarteRepository.save(eCarte);
    }

    private String genererNumeroCarte() {
        return "4000-" + (int) (Math.random() * 10000) + "-" + (int) (Math.random() * 10000) + "-" + (int) (Math.random() * 10000);
    }

    private String genererCvv() {
        return String.valueOf((int) (Math.random() * 900) + 100);
    }
}

