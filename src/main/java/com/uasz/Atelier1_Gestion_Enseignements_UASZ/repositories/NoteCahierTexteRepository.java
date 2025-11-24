package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.NoteCahierTexte;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteCahierTexteRepository extends JpaRepository<NoteCahierTexte, Long> {

    List<NoteCahierTexte> findBySeance(Seance seance);

    Optional<NoteCahierTexte> findBySeanceId(Long seanceId);

    List<NoteCahierTexte> findByEnseignant(Enseignant enseignant);

    List<NoteCahierTexte> findByEstValide(boolean estValide);

    // Note: Seance n'a pas de relation avec Classe, cette méthode est commentée
    // List<NoteCahierTexte> findBySeanceClasseId(Long classeId);

    List<NoteCahierTexte> findBySeanceEcModuleId(Long moduleId);

    List<NoteCahierTexte> findByTitreContainingIgnoreCase(String titre);

    // Tri par date de séance et EC (module)
    @Query("SELECT n FROM NoteCahierTexte n LEFT JOIN n.seance s LEFT JOIN s.ec ec LEFT JOIN ec.module m ORDER BY s.dateSeance DESC, m.libelle ASC")
    List<NoteCahierTexte> findAllOrderByDateAndEC();

    // Filtrage par enseignant avec tri
    @Query("SELECT n FROM NoteCahierTexte n LEFT JOIN n.seance s LEFT JOIN s.ec ec LEFT JOIN ec.module m WHERE n.enseignant.id = :enseignantId ORDER BY s.dateSeance DESC, m.libelle ASC")
    List<NoteCahierTexte> findByEnseignantIdOrderByDateAndEC(@Param("enseignantId") Long enseignantId);

    // Filtrage par semestre - Note: Module n'a pas de relation avec Semestre, cette requête est simplifiée
    @Query("SELECT n FROM NoteCahierTexte n LEFT JOIN n.seance s ORDER BY s.dateSeance DESC")
    List<NoteCahierTexte> findBySemestre(@Param("semestre") Long semestre);

    // Filtrage combiné
    @Query("SELECT n FROM NoteCahierTexte n LEFT JOIN n.seance s LEFT JOIN s.ec ec LEFT JOIN ec.module m WHERE " +
           "(:enseignantId IS NULL OR n.enseignant.id = :enseignantId) AND " +
           "(:moduleId IS NULL OR m.id = :moduleId) " +
           "ORDER BY s.dateSeance DESC, m.libelle ASC")
    List<NoteCahierTexte> findWithFilters(@Param("enseignantId") Long enseignantId,
                                          @Param("moduleId") Long moduleId,
                                          @Param("semestre") Long semestre);
}
