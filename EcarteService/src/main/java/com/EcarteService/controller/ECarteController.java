package com.EcarteService.controller;


import com.EcarteService.model.ECarte;
import com.EcarteService.service.ECarteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ecarte")
public class ECarteController {

    @Autowired
    private ECarteService eCarteService;

    @PostMapping("/generer")
    public ResponseEntity<?> genererECarte(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }

        try {
            ECarte eCarte = eCarteService.genererECarte(email);
            return ResponseEntity.ok(eCarte);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }


}
