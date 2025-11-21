package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.EC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ECRepository extends JpaRepository<EC, Long> {
    Optional<EC> findByCode(String code);
    List<EC> findByUeId(Long ueId);
    List<EC> findByLibelleContainingIgnoreCase(String libelle);
    boolean existsByCode(String code);
}