package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.PlanningDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.SeanceDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Salle;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.SalleRepository;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.SeanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanningService {

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private SalleRepository salleRepository;

    /**
     * US33 - Récupérer les plannings des salles
     */
    public PlanningDTO getPlanningBySalle(Long salleId) {
        Salle salle = salleRepository.findById(salleId)
                .orElseThrow(() -> new EntityNotFoundException("Salle non trouvée avec l'id: " + salleId));

        List<Seance> seances = seanceRepository.findBySalleId(salleId);

        PlanningDTO planningDTO = new PlanningDTO();
        planningDTO.setTitle("Emploi du temps pour la salle : " + salle.getLibelle());

        List<SeanceDTO> seanceDTOs = seances.stream()
                .map(this::mapToSeanceDTO)
                .sorted(Comparator.comparing(SeanceDTO::getHeureDebut))
                .collect(Collectors.toList());

        seanceDTOs.forEach(dto -> planningDTO.addSeance(dto.getDateSeance(), dto));

        return planningDTO;
    }

    /**
     * US33 - Récupérer les plannings des salles pour une période
     */
    public List<Seance> getPlanningSalleByPeriode(Long salleId, LocalDate dateDebut, LocalDate dateFin) {
        return seanceRepository.findBySalleIdAndDateSeanceBetween(salleId, dateDebut, dateFin);
    }

    /**
     * US37 - Rechercher les séances d'une semaine (lundi à samedi)
     */
    public List<Seance> getSeancesSemaine(LocalDate date) {
        LocalDate lundi = date.with(DayOfWeek.MONDAY);
        LocalDate samedi = date.with(DayOfWeek.SATURDAY);
        return seanceRepository.findByDateSeanceBetweenOrderByDateSeanceAscHeureDebutAsc(lundi, samedi);
    }

    /**
     * US37 - Rechercher les séances d'une semaine pour un enseignant
     */
    public List<Seance> getSeancesSemaineEnseignant(Long enseignantId, LocalDate date) {
        LocalDate lundi = date.with(DayOfWeek.MONDAY);
        LocalDate samedi = date.with(DayOfWeek.SATURDAY);
        return seanceRepository.findByEnseignantIdAndDateSeanceBetweenOrderByDateSeanceAscHeureDebutAsc(enseignantId, lundi, samedi);
    }

    /**
     * US37 - Rechercher les séances d'une semaine pour une salle
     */
    public List<Seance> getSeancesSemaineSalle(Long salleId, LocalDate date) {
        LocalDate lundi = date.with(DayOfWeek.MONDAY);
        LocalDate samedi = date.with(DayOfWeek.SATURDAY);
        return seanceRepository.findBySalleIdAndDateSeanceBetweenOrderByDateSeanceAscHeureDebutAsc(salleId, lundi, samedi);
    }

    /**
     * Récupérer toutes les séances d'une période
     */
    public List<Seance> getSeancesByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return seanceRepository.findByDateSeanceBetweenOrderByDateSeanceAscHeureDebutAsc(dateDebut, dateFin);
    }

    /**
     * Récupérer les séances du jour
     */
    public List<Seance> getSeancesJour(LocalDate date) {
        return seanceRepository.findByDateSeanceOrderByHeureDebutAsc(date);
    }

    private SeanceDTO mapToSeanceDTO(Seance seance) {
        SeanceDTO dto = new SeanceDTO();
        dto.setId(seance.getId());
        dto.setDateSeance(seance.getDateSeance());
        dto.setHeureDebut(seance.getHeureDebut());
        dto.setHeureFin(seance.getHeureFin());
        dto.setDuree(seance.getDuree());

        // Set IDs for relationships
        dto.setSalleId(seance.getSalle() != null ? seance.getSalle().getId() : null);
        dto.setEmploiId(seance.getEmploi() != null ? seance.getEmploi().getId() : null);
        dto.setRepartitionId(seance.getRepartition() != null ? seance.getRepartition().getId() : null);
        dto.setDeroulementId(seance.getDeroulement() != null ? seance.getDeroulement().getId() : null);

        // Set fields for display
        if (seance.getSalle() != null) {
            dto.setSalleNom(seance.getSalle().getLibelle());
        }
        if (seance.getEmploi() != null) {
            dto.setEmploiLibelle(seance.getEmploi().getLibelle());
        }
        if (seance.getRepartition() != null) {
            if (seance.getRepartition().getEnseignant() != null) {
                dto.setEnseignantNom(seance.getRepartition().getEnseignant().getPrenom() + " " + seance.getRepartition().getEnseignant().getNom());
            }
            if (seance.getRepartition().getEc() != null) {
                dto.setEcLibelle(seance.getRepartition().getEc().getLibelle());
            }
        }
        return dto;
    }
}
