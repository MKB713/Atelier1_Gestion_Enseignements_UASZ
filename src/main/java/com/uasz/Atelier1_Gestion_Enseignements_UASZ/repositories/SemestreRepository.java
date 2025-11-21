package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SemestreRepository extends JpaRepository<Semestre, Long> {
    Optional<Semestre> findByNom(String nom);
    List<Semestre> findByNomContainingIgnoreCase(String nom);
}
