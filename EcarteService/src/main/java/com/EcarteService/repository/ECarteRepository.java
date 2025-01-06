package com.EcarteService.repository;

import com.EcarteService.model.ECarte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ECarteRepository extends JpaRepository<ECarte, Long> {
  //  boolean existsByEmailUtilisateur(String email);

    Optional<ECarte> findByEmailUtilisateur(String email);
    Optional<ECarte> findByNumeroCarte(String numeroCarte);
    Optional<ECarte> findByNomClient(String nomClient);
    Optional<ECarte> findByWalletId(Long walletId); // Assuming walletId represents the user ID

}
