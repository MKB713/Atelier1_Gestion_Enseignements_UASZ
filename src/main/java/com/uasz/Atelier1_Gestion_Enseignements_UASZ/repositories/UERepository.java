package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Ue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UERepository extends JpaRepository<Ue, Long> {
    Optional<Ue> findByCode(String code);
    List<Ue> findByFormationId(Long formationId);
    List<Ue> findByModuleId(Long moduleId); // AJOUT
    List<Ue> findByLibelleContainingIgnoreCase(String libelle);
    boolean existsByCode(String code);
}

