package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.ChoixEnseignement;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutChoix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChoixEnseignementRepository extends JpaRepository<ChoixEnseignement, Long> {

    // Trouver les choix d'un enseignant
    List<ChoixEnseignement> findByEnseignantId(Long enseignantId);

    // Trouver les choix d'un enseignant par semestre
    List<ChoixEnseignement> findByEnseignantIdAndSemestre(Long enseignantId, String semestre);

    // Vérifier si un enseignant a déjà fait un choix pour un semestre donné
    Optional<ChoixEnseignement> findByEnseignantIdAndSemestreAndStatutNot(
            Long enseignantId, String semestre, StatutChoix statut);

    // Trouver les choix par statut
    List<ChoixEnseignement> findByStatut(StatutChoix statut);

    // Rechercher par matricule de l'enseignant (US32)
    @Query("SELECT c FROM ChoixEnseignement c WHERE c.enseignant.matricule = :matricule")
    List<ChoixEnseignement> findByEnseignantMatricule(@Param("matricule") Long matricule);

    // Trouver tous les choix en attente
    List<ChoixEnseignement> findByStatutOrderByDateCreationDesc(StatutChoix statut);
}
