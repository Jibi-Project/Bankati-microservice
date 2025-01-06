package com.User_Auth_service.controller;

import com.User_Auth_service.model.Creancier;
import com.User_Auth_service.repository.CreancierRepository;
import com.User_Auth_service.service.CreancierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/creanciers")
@CrossOrigin(origins = "http://localhost:4200") // Allow only specific origin
public class CreancierController {
    @Autowired
    private CreancierRepository creancierRepository;

    @Autowired
    private CreancierService creancierService;

    @GetMapping
    public List<Creancier> getAllCreanciers() {
        return creancierRepository.findAll();
    }

    @PostMapping
    public Creancier createCreancier(@RequestBody Creancier creancier) {
        return creancierRepository.save(creancier);
    }
    @GetMapping("/grouped")
    public Map<String, List<Creancier>> getGroupedCreanciers() {
        return creancierRepository.findAll().stream()
                .collect(Collectors.groupingBy(Creancier::getType));
    }

    @PostMapping("/getByEmail")
    public ResponseEntity<Creancier> getCreancierByEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        Creancier creancier = creancierService.getCreancierByEmail(email);

        if (creancier != null) {
            return ResponseEntity.ok(creancier);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
