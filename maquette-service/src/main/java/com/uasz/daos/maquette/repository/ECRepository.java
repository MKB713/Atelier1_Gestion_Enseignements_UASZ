package com.uasz.daos.maquette.repository;

import com.uasz.daos.maquette.model.EC;
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
