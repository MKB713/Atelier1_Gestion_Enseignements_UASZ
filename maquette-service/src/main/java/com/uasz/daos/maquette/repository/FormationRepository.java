package com.uasz.daos.maquette.repository;

import com.uasz.daos.maquette.model.Formation;
import com.uasz.daos.maquette.enums.StatutFormation;
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

    // Ajouts pour archivage et recherche
    List<Formation> findByStatutFormation(StatutFormation statutFormation);
    List<Formation> findByLibelleContainingIgnoreCase(String libelle);
}
