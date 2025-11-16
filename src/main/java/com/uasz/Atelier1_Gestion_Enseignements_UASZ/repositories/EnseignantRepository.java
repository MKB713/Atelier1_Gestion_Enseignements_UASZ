package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutEnseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
    Optional<Enseignant> findByMatricule(Long matricule);
    Optional<Enseignant> findByEmail(String email);
    List<Enseignant> findByEmailAndIdIsNot(String email, Long id);
    List<Enseignant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
    List<Enseignant> findByEstActif(boolean estActif);
    List<Enseignant> findByGrade(String grade);
    List<Enseignant> findByStatutEnseignant(StatutEnseignant statut);
    List<Enseignant> findByStatutEnseignantNot(StatutEnseignant statut);
}
