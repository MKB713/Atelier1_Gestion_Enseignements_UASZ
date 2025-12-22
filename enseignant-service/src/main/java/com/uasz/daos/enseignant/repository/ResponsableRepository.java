package com.uasz.daos.enseignant.repository;

import com.uasz.daos.enseignant.model.Responsable;
import com.uasz.daos.enseignant.enums.TypeResponsable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponsableRepository extends JpaRepository<Responsable, Long> {

    // Recherche par email
    Optional<Responsable> findByEmail(String email);

    // Vérifier si l'email existe
    boolean existsByEmail(String email);

    // Recherche par type (LICENCE ou MASTER)
    List<Responsable> findByType(TypeResponsable type);

    // Recherche par formation
    List<Responsable> findByFormationId(Long formationId);

    // Recherche des responsables actifs
    List<Responsable> findByActifTrue();

    // Recherche par type et actif
    List<Responsable> findByTypeAndActifTrue(TypeResponsable type);

    // Recherche par nom ou prénom
    @Query("SELECT r FROM Responsable r WHERE " +
           "LOWER(r.nom) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(r.prenom) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(r.email) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Responsable> rechercherResponsables(@Param("term") String term);

    // Trouver le responsable actif d'une formation
    @Query("SELECT r FROM Responsable r WHERE r.formationId = :formationId AND r.actif = true")
    Optional<Responsable> findResponsableActifByFormation(@Param("formationId") Long formationId);
}
