package com.example.wallet_service.client;

import com.example.wallet_service.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "USER-AUTH-SERVICE", url = "${application.config.users-url}")
public interface UserClient {
    @GetMapping("/users/email/{email}")
    Optional<User> findUserByEmail(@PathVariable("email") String email);


    @GetMapping("/users/{id}")
    Optional<User> findUserById(@PathVariable("id") int id);
}
