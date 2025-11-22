package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Coordinateur;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoordinateurRepository extends JpaRepository<Coordinateur, Long> {

    // Recherche par email
    Optional<Coordinateur> findByEmail(String email);

    // Vérifier si l'email existe
    boolean existsByEmail(String email);

    // Recherche par formation
    List<Coordinateur> findByFormation(Formation formation);

    // Recherche des coordinateurs actifs
    List<Coordinateur> findByActifTrue();

    // Recherche par nom ou prénom
    @Query("SELECT c FROM Coordinateur c WHERE " +
           "LOWER(c.nom) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(c.prenom) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Coordinateur> rechercherCoordinateurs(@Param("term") String term);

    // Trouver le coordinateur actif d'une formation
    @Query("SELECT c FROM Coordinateur c WHERE c.formation.id = :formationId AND c.actif = true")
    Optional<Coordinateur> findCoordinateurActifByFormation(@Param("formationId") Long formationId);
}
