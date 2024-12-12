package com.User_Auth_service.controller;

import com.User_Auth_service.model.ECarte;
import com.User_Auth_service.service.ECarteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
