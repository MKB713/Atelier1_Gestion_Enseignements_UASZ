package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    Optional<Module> findByCode(String code);
    List<Module> findByFormationId(Long formationId);
    List<Module> findByMaquetteId(Long maquetteId); // AJOUT
    List<Module> findBySemestreAndFormationId(int semestre, Long formationId);
    List<Module> findByLibelleContainingIgnoreCase(String libelle);
    boolean existsByCode(String code);
}