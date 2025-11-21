package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    Optional<Formation> findByCode(String code);
    Optional<Formation> findByLibelle(String libelle);
    List<Formation> findByFiliereId(Long filiereId);
    List<Formation> findByNiveauId(Long niveauId);
}