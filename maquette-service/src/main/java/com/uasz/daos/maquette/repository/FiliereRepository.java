package com.uasz.daos.maquette.repository;

import com.uasz.daos.maquette.model.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FiliereRepository extends JpaRepository<Filiere, Long> {
    Optional<Filiere> findByLibelle(String libelle);
    boolean existsByLibelle(String libelle);
}
