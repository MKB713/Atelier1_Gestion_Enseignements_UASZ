package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UERepository extends JpaRepository<UE, Long> {
    Optional<UE> findByCode(String code);
    List<UE> findByFormationId(Long formationId);
    List<UE> findByModuleId(Long moduleId); // AJOUT
    List<UE> findByLibelleContainingIgnoreCase(String libelle);
    boolean existsByCode(String code);
}

