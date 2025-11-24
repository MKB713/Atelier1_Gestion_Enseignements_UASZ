package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.*;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    Optional<Module> findByCode(String code);
    Optional<Module> findByLibelle(String libelle);
    List<Module> findByUeId(Long ueId);
}