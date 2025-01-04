package com.User_Auth_service.controller;


import com.User_Auth_service.dto.ReqRes;
import com.User_Auth_service.model.User;
import com.User_Auth_service.repository.UsersRepo;
import com.User_Auth_service.service.EmailService;
import com.User_Auth_service.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UsersManagementService usersManagementService;

    @Autowired
    private UsersRepo userRepository;

    @Autowired
    private EmailService emailService;


    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody ReqRes reg) {
        String generatedPassword = UUID.randomUUID().toString().substring(0, 8);
        reg.setPassword(generatedPassword);

        usersManagementService.register(reg);

        String emailBody = "Welcome, " + reg.getNom() + "!\n\nYour temporary password is: " + generatedPassword;
        emailService.sendEmail(reg.getEmail(), "Welcome to BankatiApp", emailBody);

        return ResponseEntity.ok("User registered successfully. Password sent to email.");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.login(req));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.refreshToken(req));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers(){
        return ResponseEntity.ok(usersManagementService.getAllUsers());

    }

    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<ReqRes> getUSerByID(@PathVariable Integer userId){
        return ResponseEntity.ok(usersManagementService.getUsersById(userId));

    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Integer userId, @RequestBody User reqres){
        return ResponseEntity.ok(usersManagementService.updateUser(userId, reqres));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = usersManagementService.getMyInfo(email);

        HttpStatus status = HttpStatus.valueOf(response.getStatutCode());

        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<ReqRes> deleteUSer(@PathVariable Integer userId){
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }
    @PostMapping("/auth/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newPassword = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(newPassword);
        userRepository.save(user);

        String emailBody = "Your new password is: " + newPassword;
        emailService.sendEmail(email, "Password Reset", emailBody);

        return ResponseEntity.ok("Password reset successfully. New password sent to email.");
    }
    @GetMapping("/email-by-id/{userId}")
    public ResponseEntity<String> getUserEmailById(@PathVariable Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user.getEmail());
    }

}