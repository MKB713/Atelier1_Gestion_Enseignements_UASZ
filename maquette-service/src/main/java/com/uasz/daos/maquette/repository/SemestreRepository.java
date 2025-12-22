package com.uasz.daos.maquette.repository;
import com.uasz.daos.maquette.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SemestreRepository extends JpaRepository<Semestre, Long> {
    Optional<Semestre> findByNom(String nom);
    List<Semestre> findByNomContainingIgnoreCase(String nom);
}
