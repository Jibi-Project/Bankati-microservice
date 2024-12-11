package com.User_Auth_service.repository;

import com.User_Auth_service.model.ECarte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ECarteRepository extends JpaRepository<ECarte, Long> {
}
