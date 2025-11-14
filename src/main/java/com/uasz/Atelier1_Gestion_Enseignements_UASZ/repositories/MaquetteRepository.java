package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MaquetteRepository extends JpaRepository<Maquette, Long> {
    Optional<Maquette> findByNom(String nom);
    List<Maquette> findByNomContainingIgnoreCase(String nom);
}
