package com.User_Auth_service.service;


import com.User_Auth_service.dto.ReqRes;
import com.User_Auth_service.model.User;
import com.User_Auth_service.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UsersManagementService {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> getUserById(int id) {
        return usersRepo.findById(id);
    }


    public ReqRes register(ReqRes registrationRequest){
        ReqRes resp = new ReqRes();

        try {
            User ourUser = new User();
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setAdresse(registrationRequest.getAdresse());
            ourUser.setRole(registrationRequest.getRole());
            ourUser.setNom(registrationRequest.getNom());
            ourUser.setPrenom(registrationRequest.getPrenom());
            ourUser.setTelephone(registrationRequest.getTelephone());
                //here should generate a pwd
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            User ourUsersResult = usersRepo.save(ourUser);
            if (ourUsersResult.getId()>0) {
                resp.setOurUsers((ourUsersResult));
                resp.setMessage("User Saved Successfully");
                resp.setStatutCode(200);
            }

        }catch (Exception e){
            resp.setStatutCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }


    public ReqRes login(ReqRes loginRequest){
        ReqRes response = new ReqRes();
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword()));
            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatutCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

        }catch (Exception e){
            response.setStatutCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }



    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        try {
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            User user = usersRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
                String jwt = jwtUtils.generateToken(user);
                response.setStatutCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            } else {
                // Handle invalid token scenario
                response.setStatutCode(401);
                response.setMessage("Invalid or expired refresh token");
            }
        } catch (Exception e) {
            response.setStatutCode(500);
            response.setMessage("Error refreshing token: " + e.getMessage());
        }
        return response;
    }

    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();

        try {
            List<User> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                reqRes.setOurUsersList(result);
                reqRes.setStatutCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatutCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatutCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }


    public ReqRes getUsersById(Integer id) {
        ReqRes reqRes = new ReqRes();
        try {
            User usersById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setOurUsers(usersById);
            reqRes.setStatutCode(200);
            reqRes.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatutCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }


    public ReqRes deleteUser(Integer userId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                usersRepo.deleteById(userId);
                reqRes.setStatutCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatutCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatutCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes updateUser(Integer userId, User updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();

                // Update fields only if they are provided (non-null or non-empty)
                if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                    existingUser.setEmail(updatedUser.getEmail());
                }
                if (updatedUser.getNom() != null && !updatedUser.getNom().isEmpty()) {
                    existingUser.setNom(updatedUser.getNom());
                }
                if (updatedUser.getPrenom() != null && !updatedUser.getPrenom().isEmpty()) {
                    existingUser.setPrenom(updatedUser.getPrenom());
                }
                if (updatedUser.getTelephone() != null && !updatedUser.getTelephone().isEmpty()) {
                    existingUser.setTelephone(updatedUser.getTelephone());
                }
                if (updatedUser.getAdresse() != null && !updatedUser.getAdresse().isEmpty()) {
                    existingUser.setAdresse(updatedUser.getAdresse());
                }
                if (updatedUser.getRole() != null && !updatedUser.getRole().isEmpty()) {
                    existingUser.setRole(updatedUser.getRole());
                }

                // Save the updated user
                User savedUser = usersRepo.save(existingUser);
                reqRes.setOurUsers(savedUser);
                reqRes.setStatutCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatutCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatutCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }


    public ReqRes getMyInfo(String email){
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setOurUsers(userOptional.get());
                reqRes.setStatutCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatutCode(404);
                reqRes.setMessage("User not found for update");
            }

        }catch (Exception e){
            reqRes.setStatutCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;

    }

    // Service Method to Change Password
    public ReqRes changePassword(String oldPassword, String newPassword, String email) {
        ReqRes resp = new ReqRes();
        try {
            // Find the user by email using Optional
            Optional<User> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Check if the old password matches
                if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                    resp.setStatutCode(400);
                    resp.setMessage("Old password is incorrect");
                    return resp;
                }

                // Update the password
                user.setPassword(passwordEncoder.encode(newPassword));
                usersRepo.save(user);

                resp.setStatutCode(200);
                resp.setMessage("Password updated successfully");
                resp.setOurUsers(user);
            } else {
                resp.setStatutCode(404);
                resp.setMessage("User not found");
            }
        } catch (Exception e) {
            resp.setStatutCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes lockOrUnlockUser(Integer userId, boolean lock) {
        ReqRes resp = new ReqRes();

        try {
            Optional<User> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setAccountNonLocked(!lock); // Lock if `lock=true`, unlock if `lock=false`
                usersRepo.save(user);

                resp.setOurUsers(user);
                resp.setMessage(lock ? "User account locked" : "User account unlocked");
                resp.setStatutCode(200);
            } else {
                resp.setMessage("User not found");
                resp.setStatutCode(404);
            }
        } catch (Exception e) {
            resp.setError(e.getMessage());
            resp.setStatutCode(500);
        }

        return resp;
    }


}
/*  for later to generate pwd
 public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

    public static void main(String[] args) {
        String randomString = generateRandomString(8);
        System.out.println("Random String: " + randomString);
    }
*
* */