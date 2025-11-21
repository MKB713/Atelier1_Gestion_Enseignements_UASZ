package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Maquette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaquetteRepository extends JpaRepository<Maquette, Long> {
    List<Maquette> findByFormationId(Long formationId);
    List<Maquette> findByAnneeAcademique(int anneeAcademique);
    Optional<Maquette> findByFormationIdAndAnneeAcademique(Long formationId, int anneeAcademique);
    List<Maquette> findByActiveTrue();
    List<Maquette> findByLibelleContainingIgnoreCase(String libelle);
}