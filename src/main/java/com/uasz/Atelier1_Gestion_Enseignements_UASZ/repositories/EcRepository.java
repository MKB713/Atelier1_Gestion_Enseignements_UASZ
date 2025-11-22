package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EcRepository extends JpaRepository<Ec, Long> {
    Optional<Ec> findByCode(String code);
    Optional<Ec> findByLibelle(String libelle);
    List<Ec> findByUeId(Long ueId);
    List<Ec> findByLibelleContainingIgnoreCase(String libelle);
}