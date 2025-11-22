package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Classe;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Etudiant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Formation;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Inscription;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutInscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    // Recherche par étudiant
    List<Inscription> findByEtudiant(Etudiant etudiant);

    // Recherche par classe
    List<Inscription> findByClasse(Classe classe);

    // Recherche par formation
    List<Inscription> findByFormation(Formation formation);

    // Recherche par année académique
    List<Inscription> findByAnneeAcademique(String anneeAcademique);

    // Recherche par statut
    List<Inscription> findByStatut(StatutInscription statut);

    // Recherche des inscriptions actives d'un étudiant
    @Query("SELECT i FROM Inscription i WHERE i.etudiant.id = :etudiantId AND i.statut = 'EN_COURS'")
    List<Inscription> findInscriptionsActivesEtudiant(@Param("etudiantId") Long etudiantId);

    // Recherche d'une inscription spécifique (étudiant + classe + année)
    @Query("SELECT i FROM Inscription i WHERE i.etudiant.id = :etudiantId " +
           "AND i.classe.id = :classeId AND i.anneeAcademique = :annee")
    Optional<Inscription> findByEtudiantAndClasseAndAnnee(
        @Param("etudiantId") Long etudiantId,
        @Param("classeId") Long classeId,
        @Param("annee") String annee
    );

    // Compter les inscriptions par classe
    long countByClasse(Classe classe);

    // Compter les inscriptions par formation et année
    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.formation.id = :formationId " +
           "AND i.anneeAcademique = :annee")
    long countByFormationAndAnnee(@Param("formationId") Long formationId, @Param("annee") String annee);

    // Trouver les redoublants d'une classe
    @Query("SELECT i FROM Inscription i WHERE i.classe.id = :classeId AND i.estRedoublant = true")
    List<Inscription> findRedoublantsParClasse(@Param("classeId") Long classeId);
}
