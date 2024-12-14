package com.EcarteService.client;

import com.EcarteService.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-service", url = "${application.config.users-url}")
public interface UserClient {
    @GetMapping("/users/email/{email}")
    Optional<User> findUserByEmail(@PathVariable("email") String email);

}
