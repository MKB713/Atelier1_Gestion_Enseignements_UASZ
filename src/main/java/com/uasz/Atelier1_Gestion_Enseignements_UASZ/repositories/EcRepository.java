package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.EC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ECRepository extends JpaRepository<EC, Long> {
    List<EC> findByLibelleContainingIgnoreCase(String keyword);
    List<EC> findByArchive(boolean archive);
    // Rechercher par libellé et exclure les archivés
    List<EC> findByLibelleContainingIgnoreCaseAndArchive(String keyword, boolean archive);
}
