package com.User_Auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

    @Autowired
    private RestTemplate restTemplate;

    public void sendEmail(String to, String subject, String message) {
        try {
            // Construct the URL with query parameters
            String emailUrl = "http://localhost:1013/email/send?to=" + to + "&subject=" + subject + "&message=" + message;

            // Send POST request with no body (query params only)
            restTemplate.postForObject(emailUrl, null, String.class);

            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
