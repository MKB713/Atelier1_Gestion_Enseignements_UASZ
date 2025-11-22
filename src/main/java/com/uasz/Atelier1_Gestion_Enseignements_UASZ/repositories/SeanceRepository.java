package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findBySalleId(Long salleId);

    List<Seance> findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfter(
            Long salleId, LocalDate dateSeance, LocalTime heureFin, LocalTime heureDebut);

    List<Seance> findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfterAndIdNot(
            Long salleId, LocalDate dateSeance, LocalTime heureFin, LocalTime heureDebut, Long seanceId);

    @Query("SELECT s FROM Seance s JOIN s.repartition r WHERE r.enseignant.id = :enseignantId")
    List<Seance> findByEnseignantId(@Param("enseignantId") Long enseignantId);

    @Query("SELECT s FROM Seance s JOIN s.repartition r WHERE r.enseignant.id = :enseignantId AND s.dateSeance = :dateSeance AND s.heureDebut < :heureFin AND s.heureFin > :heureDebut")
    List<Seance> findTeacherConflicts(@Param("enseignantId") Long enseignantId, @Param("dateSeance") LocalDate dateSeance, @Param("heureDebut") LocalTime heureDebut, @Param("heureFin") LocalTime heureFin);

    @Query("SELECT s FROM Seance s JOIN s.repartition r WHERE r.enseignant.id = :enseignantId AND s.dateSeance = :dateSeance AND s.heureDebut < :heureFin AND s.heureFin > :heureDebut AND s.id <> :seanceId")
    List<Seance> findTeacherConflictsExcludingId(@Param("enseignantId") Long enseignantId, @Param("dateSeance") LocalDate dateSeance, @Param("heureDebut") LocalTime heureDebut, @Param("heureFin") LocalTime heureFin, @Param("seanceId") Long seanceId);

    List<Seance> findByDateSeanceBetweenOrderByDateSeanceAscHeureDebutAsc(LocalDate start, LocalDate end);
}
