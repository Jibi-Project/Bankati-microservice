package com.User_Auth_service.repository;


import com.User_Auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'USER'")
    long countUsersWithRoleUser();
}