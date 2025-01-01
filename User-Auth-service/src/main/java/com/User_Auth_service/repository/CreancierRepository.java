package com.User_Auth_service.repository;

import com.User_Auth_service.model.Creancier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreancierRepository extends JpaRepository<Creancier, Long> {
}

