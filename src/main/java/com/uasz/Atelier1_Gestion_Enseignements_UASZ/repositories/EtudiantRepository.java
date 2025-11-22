package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Etudiant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutEtudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    // Recherche par matricule
    Optional<Etudiant> findByMatricule(String matricule);

    // Vérifier si le matricule existe
    boolean existsByMatricule(String matricule);

    // Vérifier si l'email existe
    boolean existsByEmail(String email);

    // Recherche par email
    Optional<Etudiant> findByEmail(String email);

    // Recherche par statut
    List<Etudiant> findByStatut(StatutEtudiant statut);

    // Recherche par nom ou prénom
    @Query("SELECT e FROM Etudiant e WHERE " +
           "LOWER(e.nom) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(e.prenom) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(e.matricule) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Etudiant> rechercherEtudiants(@Param("term") String term);

    // Compter les étudiants actifs
    long countByStatut(StatutEtudiant statut);

    // Trouver les étudiants par sexe
    List<Etudiant> findBySexe(String sexe);
}
