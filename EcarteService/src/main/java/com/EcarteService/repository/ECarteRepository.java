package com.EcarteService.repository;

import com.EcarteService.model.ECarte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ECarteRepository extends JpaRepository<ECarte, Long> {
}
