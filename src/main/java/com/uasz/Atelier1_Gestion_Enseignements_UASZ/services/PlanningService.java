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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanningService {

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private SalleRepository salleRepository;

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
