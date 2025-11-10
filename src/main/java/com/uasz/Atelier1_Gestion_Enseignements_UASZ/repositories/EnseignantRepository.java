package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

    // Recherche par matricule
    Optional<Enseignant> findByMatricule(Long matricule);

    // Recherche par email
    Optional<Enseignant> findByEmail(String email);


}
