package com.User_Auth_service.service;

import com.User_Auth_service.model.User;
import com.User_Auth_service.repository.ECarteRepository;
import com.User_Auth_service.model.ECarte;
import com.User_Auth_service.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ECarteService {

    @Autowired
    private ECarteRepository eCarteRepository;
    @Autowired
    private UsersRepo usersRepo;

    public ECarte genererECarte(String email) {
        User utilisateur = usersRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        ECarte eCarte = new ECarte();
        eCarte.setNumeroCarte(genererNumeroCarte());
        eCarte.setDateExpiration(LocalDate.now().plusYears(1));
        eCarte.setCvv(genererCvv());
        eCarte.setUser(utilisateur);

        return eCarteRepository.save(eCarte);
    }

    private String genererNumeroCarte() {
        return "4000-" + (int) (Math.random() * 10000) + "-" + (int) (Math.random() * 10000) + "-" + (int) (Math.random() * 10000);
    }

    private String genererCvv() {
        return String.valueOf((int) (Math.random() * 900) + 100);
    }
}

