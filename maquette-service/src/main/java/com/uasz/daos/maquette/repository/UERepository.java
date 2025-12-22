package com.uasz.daos.maquette.repository;

import com.uasz.daos.maquette.model.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UERepository extends JpaRepository<UE, Long> {
    Optional<UE> findByCode(String code);
    // Pour récupérer les archives
    List<UE> findByArchive(boolean archive);
}
