package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UeRepository extends JpaRepository<Ue, Long> {
    Optional<Ue> findByCode(String code);
    Optional<Ue> findByLibelle(String libelle);
    List<Ue> findByLibelleContainingIgnoreCase(String libelle);
}
