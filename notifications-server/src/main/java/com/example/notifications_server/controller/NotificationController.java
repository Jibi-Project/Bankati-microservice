package com.example.notifications_server.controller;

import com.example.notifications_server.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class NotificationController {

    @Autowired
    private EmailService emailService;

    /**
     * Accepts query parameters instead of JSON body.
     *
     * URL example:
     * http://localhost:1013/email/send?to=user@example.com&subject=Welcome&message=Hello%20World
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message) {

        emailService.sendEmail(to, subject, message);
        return ResponseEntity.ok("Email sent successfully to: " + to);
    }
}
