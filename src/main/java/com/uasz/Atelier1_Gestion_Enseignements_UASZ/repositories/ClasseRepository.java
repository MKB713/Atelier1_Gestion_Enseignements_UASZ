package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Classe;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Filiere;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    Optional<Classe> findByCode(String code);

    Optional<Classe> findByLibelle(String libelle);

    List<Classe> findByCodeAndIdIsNot(String code, Long id);

    List<Classe> findByFiliereId(Long filiereId);

    List<Classe> findByNiveauId(Long niveauId);

    List<Classe> findByFiliere(Filiere filiere);

    List<Classe> findByNiveau(Niveau niveau);

    List<Classe> findByAnneeAcademique(String anneeAcademique);

    List<Classe> findByEstActive(boolean estActive);

    List<Classe> findByEstArchivee(boolean estArchivee);

    List<Classe> findByCodeContainingIgnoreCaseOrLibelleContainingIgnoreCase(String code, String libelle);

    List<Classe> findByFiliereIdAndNiveauId(Long filiereId, Long niveauId);

    List<Classe> findByEstActiveAndEstArchivee(boolean estActive, boolean estArchivee);
}
