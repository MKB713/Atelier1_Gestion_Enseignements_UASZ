package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findBySalleId(Long salleId);

    List<Seance> findByEnseignantIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfter(
            Long enseignantId, LocalDate dateSeance, LocalTime heureFin, LocalTime heureDebut);

    List<Seance> findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfter(
            Long salleId, LocalDate dateSeance, LocalTime heureFin, LocalTime heureDebut);

    List<Seance> findByEnseignantIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfterAndIdNot(
            Long enseignantId, LocalDate dateSeance, LocalTime heureFin, LocalTime heureDebut, Long seanceId);

    List<Seance> findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfterAndIdNot(
            Long salleId, LocalDate dateSeance, LocalTime heureFin, LocalTime heureDebut, Long seanceId);

    List<Seance> findByEnseignantId(Long enseignantId);
}
