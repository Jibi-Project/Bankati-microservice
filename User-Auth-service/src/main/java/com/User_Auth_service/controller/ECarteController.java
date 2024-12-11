package com.User_Auth_service.controller;

import com.User_Auth_service.model.ECarte;
import com.User_Auth_service.service.ECarteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ecarte")
public class ECarteController {

    @Autowired
    private ECarteService eCarteService;

    @PostMapping("/generer")
    public ResponseEntity<ECarte> genererECarte(@RequestParam String email) {
        ECarte eCarte = eCarteService.genererECarte(email);
        return ResponseEntity.ok(eCarte);
    }
}

