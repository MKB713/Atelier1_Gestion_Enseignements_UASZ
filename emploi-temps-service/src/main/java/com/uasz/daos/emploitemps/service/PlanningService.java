package com.uasz.daos.emploitemps.service;

import com.uasz.daos.emploitemps.dto.PlanningDTO;
import com.uasz.daos.emploitemps.dto.SeanceDTO;
import com.uasz.daos.emploitemps.model.Salle;
import com.uasz.daos.emploitemps.model.Seance;
import com.uasz.daos.emploitemps.repository.SalleRepository;
import com.uasz.daos.emploitemps.repository.SeanceRepository;
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
        dto.setDateSeance(seance.getDateSeance());
        dto.setHeureDebut(seance.getHeureDebut());
        dto.setHeureFin(seance.getHeureFin());
        dto.setSalleId(seance.getSalle().getId());
        dto.setSalleNom(seance.getSalle().getLibelle());
        dto.setEnseignantId(seance.getEnseignantId());
        dto.setEcId(seance.getEcId());
        return dto;
    }
}
