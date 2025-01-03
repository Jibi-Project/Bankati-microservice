package com.EcarteService.controller;


import com.EcarteService.model.ECarte;
import com.EcarteService.service.ECarteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

   /* @PostMapping("/transaction")
    public ResponseEntity<String> doTransaction(@RequestBody Map<String, Object> payload) {
        try {
            String senderNumeroCarte = (String) payload.get("senderNumeroCarte");
            String receiverNumeroCarte = (String) payload.get("receiverNumeroCarte");
            Double amount = Double.valueOf(payload.get("amount").toString());

            String result = eCarteService.doTransaction(senderNumeroCarte, receiverNumeroCarte, amount);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }*/
   @PostMapping("/transaction")
   public ResponseEntity<String> doTransaction(@RequestBody Map<String, Object> payload) {
       String senderNumeroCarte = (String) payload.get("senderNumeroCarte");
       String receiverNumeroCarte = (String) payload.get("receiverNumeroCarte");
       Double amount = Double.valueOf(payload.get("amount").toString());

       System.out.println("Sender: " + senderNumeroCarte);
       System.out.println("Receiver: " + receiverNumeroCarte);
       System.out.println("Amount: " + amount);

       try {
           String result = eCarteService.doTransaction(senderNumeroCarte, receiverNumeroCarte, amount);
           return ResponseEntity.ok(result);
       } catch (RuntimeException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }
   }



}
