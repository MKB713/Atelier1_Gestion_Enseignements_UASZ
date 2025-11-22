package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.SeanceDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.*;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.exceptions.ConflictException;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;

@Service
public class SeanceService {

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private SalleRepository salleRepository;

    @Autowired
    private EmploiRepository emploiRepository;

    @Autowired
    private RepartitionRepository repartitionRepository;

    @Autowired
    private DeroulementRepository deroulementRepository;

    public List<Seance> getAllSeances() {
        return seanceRepository.findAll();
    }

    public Seance getSeanceById(Long id) {
        return seanceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Séance non trouvée avec l'id: " + id));
    }

    public Seance createSeance(SeanceDTO seanceDTO) {
        Repartition repartition = repartitionRepository.findById(seanceDTO.getRepartitionId())
                .orElseThrow(() -> new EntityNotFoundException("Repartition non trouvée avec l'id: " + seanceDTO.getRepartitionId()));
        Long enseignantId = repartition.getEnseignant().getId();

        // Vérifier les conflits pour l'enseignant via la nouvelle méthode
        List<Seance> teacherConflicts = seanceRepository.findTeacherConflicts(
                enseignantId, seanceDTO.getDateSeance(), seanceDTO.getHeureDebut(), seanceDTO.getHeureFin());
        if (!teacherConflicts.isEmpty()) {
            throw new ConflictException("Conflit d'horaire : L'enseignant est déjà occupé à ce créneau.");
        }

        // Vérifier les conflits pour la salle (l'ancienne méthode est toujours valide mais les paramètres sont inversés)
        List<Seance> roomConflicts = seanceRepository.findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfter(
                seanceDTO.getSalleId(), seanceDTO.getDateSeance(), seanceDTO.getHeureFin(), seanceDTO.getHeureDebut());
        if (!roomConflicts.isEmpty()) {
            throw new ConflictException("Conflit d'horaire : La salle est déjà occupée à ce créneau.");
        }

        // Mapper le DTO à l'entité
        Seance seance = new Seance();
        seance.setDateSeance(seanceDTO.getDateSeance());
        seance.setHeureDebut(seanceDTO.getHeureDebut());
        seance.setHeureFin(seanceDTO.getHeureFin());
        seance.setDuree(seanceDTO.getDuree());

        Salle salle = salleRepository.findById(seanceDTO.getSalleId())
                .orElseThrow(() -> new EntityNotFoundException("Salle non trouvée avec l'id: " + seanceDTO.getSalleId()));
        seance.setSalle(salle);

        Emploi emploi = emploiRepository.findById(seanceDTO.getEmploiId())
                .orElseThrow(() -> new EntityNotFoundException("Emploi non trouvé avec l'id: " + seanceDTO.getEmploiId()));
        seance.setEmploi(emploi);

        seance.setRepartition(repartition);

        // Créer un Deroulement par défaut
        Deroulement deroulement = new Deroulement();
        deroulementRepository.save(deroulement);
        seance.setDeroulement(deroulement);

        return seanceRepository.save(seance);
    }

    public Seance updateSeance(Long id, SeanceDTO seanceDTO) {
        Seance existingSeance = getSeanceById(id);

        Repartition repartition = repartitionRepository.findById(seanceDTO.getRepartitionId())
                .orElseThrow(() -> new EntityNotFoundException("Repartition non trouvée avec l'id: " + seanceDTO.getRepartitionId()));
        Long enseignantId = repartition.getEnseignant().getId();

        // Vérifier les conflits pour l'enseignant
        List<Seance> teacherConflicts = seanceRepository.findTeacherConflictsExcludingId(
                enseignantId, seanceDTO.getDateSeance(), seanceDTO.getHeureDebut(), seanceDTO.getHeureFin(), id);
        if (!teacherConflicts.isEmpty()) {
            throw new ConflictException("Conflit d'horaire : L'enseignant est déjà occupé à ce créneau.");
        }

        // Vérifier les conflits pour la salle
        List<Seance> roomConflicts = seanceRepository.findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfterAndIdNot(
                seanceDTO.getSalleId(), seanceDTO.getDateSeance(), seanceDTO.getHeureFin(), seanceDTO.getHeureDebut(), id);
        if (!roomConflicts.isEmpty()) {
            throw new ConflictException("Conflit d'horaire : La salle est déjà occupée à ce créneau.");
        }

        // Mettre à jour l'entité existante
        existingSeance.setDateSeance(seanceDTO.getDateSeance());
        existingSeance.setHeureDebut(seanceDTO.getHeureDebut());
        existingSeance.setHeureFin(seanceDTO.getHeureFin());
        existingSeance.setDuree(seanceDTO.getDuree());

        Salle salle = salleRepository.findById(seanceDTO.getSalleId())
                .orElseThrow(() -> new EntityNotFoundException("Salle non trouvée avec l'id: " + seanceDTO.getSalleId()));
        existingSeance.setSalle(salle);

        Emploi emploi = emploiRepository.findById(seanceDTO.getEmploiId())
                .orElseThrow(() -> new EntityNotFoundException("Emploi non trouvé avec l'id: " + seanceDTO.getEmploiId()));
        existingSeance.setEmploi(emploi);

        existingSeance.setRepartition(repartition);

        return seanceRepository.save(existingSeance);
    }

    public void deleteSeance(Long id) {
        if (!seanceRepository.existsById(id)) {
            throw new EntityNotFoundException("Séance non trouvée avec l'id: " + id);
        }
        seanceRepository.deleteById(id);
    }

    public List<Seance> getBySalle(Long salleId) {
        return seanceRepository.findBySalleId(salleId);
    }

    public List<Seance> getByEnseignant(Long enseignantId) {
        return seanceRepository.findByEnseignantId(enseignantId);
    }

    public List<Seance> getSeancesByWeek(LocalDate dateInWeek) {
        LocalDate monday = dateInWeek.with(java.time.DayOfWeek.MONDAY);
        LocalDate saturday = dateInWeek.with(java.time.DayOfWeek.SATURDAY);
        return seanceRepository.findByDateSeanceBetweenOrderByDateSeanceAscHeureDebutAsc(monday, saturday);
    }

    public List<Seance> getSeancesByDateRange(LocalDate startDate, LocalDate endDate) {
        return seanceRepository.findByDateSeanceBetweenOrderByDateSeanceAscHeureDebutAsc(startDate, endDate);
    }
}
