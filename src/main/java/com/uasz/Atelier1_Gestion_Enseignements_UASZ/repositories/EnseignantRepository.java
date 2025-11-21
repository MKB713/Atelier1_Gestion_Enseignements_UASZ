package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutEnseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import ajouté
import org.springframework.data.repository.query.Param; // Import ajouté
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

    // Unicité et recherche simple par attributs
    Optional<Enseignant> findByMatricule(Long matricule);
    Optional<Enseignant> findByEmail(String email);
    List<Enseignant> findByEmailAndIdIsNot(String email, Long id);

    // Anciennes méthodes de recherche (la recherche combinée les remplace pour /lst-enseignants)
    // List<Enseignant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
    List<Enseignant> findByEstActif(boolean estActif);
    List<Enseignant> findByGrade(String grade);

    // Recherche par StatutEnseignant
    List<Enseignant> findByStatutEnseignant(StatutEnseignant statut);
    List<Enseignant> findByStatutEnseignantNot(StatutEnseignant statut);

    /**
     * Recherche combinée par Nom, Prénom ou Matricule (partiel),
     * excluant les enseignants ARCHIVE.
     * Le matricule (Long) est casté en String pour permettre la recherche LIKE.
     * * @param keyword Le terme de recherche.
     * @return Liste filtrée des enseignants actifs.
     */
    @Query("SELECT e FROM Enseignant e WHERE e.statutEnseignant <> 'ARCHIVE' AND " +
            "(LOWER(e.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.prenom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "CAST(e.matricule as string) LIKE CONCAT('%', :keyword, '%'))")
    List<Enseignant> searchByNomPrenomOrMatricule(@Param("keyword") String keyword);
}