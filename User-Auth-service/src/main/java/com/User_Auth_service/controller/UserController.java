package com.User_Auth_service.controller;


import com.User_Auth_service.dto.ChangePasswordRequest;
import com.User_Auth_service.dto.ReqRes;
import com.User_Auth_service.model.User;
import com.User_Auth_service.repository.UsersRepo;
import com.User_Auth_service.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UsersManagementService usersManagementService;

    @Autowired
    private UsersRepo userRepository;

    @GetMapping("/count-user-role")
    public long getNumberOfUsersWithRoleUser() {
        return usersManagementService.getNumberOfUsersWithRoleUser();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return usersManagementService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> regeister(@RequestBody ReqRes reg){
        return ResponseEntity.ok(usersManagementService.register(reg));
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
    public ResponseEntity<ReqRes> updateUser(@PathVariable Integer userId, @RequestBody User updatedUser) {
        try {
            ReqRes response = usersManagementService.updateUser(userId, updatedUser);
            if (response.getStatutCode() == 200) {
                return ResponseEntity.ok(response);
            } else if (response.getStatutCode() == 404) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            ReqRes errorResponse = new ReqRes();
            errorResponse.setStatutCode(500);
            errorResponse.setMessage("Unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
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

    @PostMapping("/change-password")
    public ResponseEntity<ReqRes> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal principal) {
        // `Principal` provides the logged-in user's email (or username)
        String email = principal.getName();

        ReqRes response = usersManagementService.changePassword(request.getOldPassword(), request.getNewPassword(), email);

        return ResponseEntity.status(response.getStatutCode()).body(response);
    }

    @PutMapping("/admin/lock/{id}")
    public ResponseEntity<ReqRes> lockUser(@PathVariable Integer id) {
        ReqRes response = usersManagementService.lockOrUnlockUser(id, true);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatutCode()));
    }

    @PutMapping("/admin/unlock/{id}")
    public ResponseEntity<ReqRes> unlockUser(@PathVariable Integer id) {
        ReqRes response = usersManagementService.lockOrUnlockUser(id, false);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatutCode()));
    }

}