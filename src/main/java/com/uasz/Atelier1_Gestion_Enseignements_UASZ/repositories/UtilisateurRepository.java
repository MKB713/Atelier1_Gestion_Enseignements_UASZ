package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByUsername(String username);
    Optional<Utilisateur> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}