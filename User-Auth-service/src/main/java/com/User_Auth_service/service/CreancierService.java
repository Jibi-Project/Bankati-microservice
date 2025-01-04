package com.User_Auth_service.service;

import com.User_Auth_service.model.Creancier;
import com.User_Auth_service.repository.CreancierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreancierService {

    @Autowired
    private CreancierRepository creancierRepository;

    public Creancier getCreancierByEmail(String email) {
        return creancierRepository.findByEmail(email);
    }
}
