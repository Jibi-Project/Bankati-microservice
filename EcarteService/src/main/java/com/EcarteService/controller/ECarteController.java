package com.EcarteService.controller;


import com.EcarteService.model.ECarte;
import com.EcarteService.service.ECarteService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ECarte> genererECarte(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        if (email == null) {
            return ResponseEntity.badRequest().body(null); // Or throw a custom exception
        }
        ECarte eCarte = eCarteService.genererECarte(email);
        return ResponseEntity.ok(eCarte);
    }
}
