package com.uasz.daos.maquette.repository;

import com.uasz.daos.maquette.model.Niveau;
import com.uasz.daos.maquette.enums.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NiveauRepository extends JpaRepository<Niveau, Long> {
    Optional<Niveau> findByNumero(int numero);
    List<Niveau> findByCycle(Cycle cycle);

    // AJOUTEZ CETTE MÉTHODE
    Optional<Niveau> findByNumeroAndCycle(int numero, Cycle cycle);
}
