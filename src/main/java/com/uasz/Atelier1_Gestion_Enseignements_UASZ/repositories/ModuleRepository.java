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
    Optional<Module> findByNom(String nom);
    List<Module> findByEcId(Long ecId);
    List<Module> findByMaquetteId(Long maquetteId);
    List<Module> findBySemestreId(Long semestreId);
}