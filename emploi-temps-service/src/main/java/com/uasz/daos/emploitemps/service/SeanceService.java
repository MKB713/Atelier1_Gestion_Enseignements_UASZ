package com.uasz.daos.emploitemps.service;

import com.uasz.daos.emploitemps.dto.SeanceDTO;
import com.uasz.daos.emploitemps.dto.ECDTO;
import com.uasz.daos.emploitemps.dto.EnseignantDTO;
import com.uasz.daos.emploitemps.model.Salle;
import com.uasz.daos.emploitemps.model.Seance;
import com.uasz.daos.emploitemps.exception.ConflictException;
import com.uasz.daos.emploitemps.api.MaquetteApi;
import com.uasz.daos.emploitemps.api.EnseignantApi;
import com.uasz.daos.emploitemps.repository.SalleRepository;
import com.uasz.daos.emploitemps.repository.SeanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeanceService {

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private SalleRepository salleRepository;

    public List<Seance> getAllSeances() {
        return seanceRepository.findAll();
    }

    public Seance getSeanceById(Long id) {
        return seanceRepository.findById(id).orElse(null);
    }

    public Seance createSeance(SeanceDTO seanceDTO) {
        // Vérifier les conflits pour l'enseignant
        List<Seance> teacherConflicts = seanceRepository.findByEnseignantIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfter(
                seanceDTO.getEnseignantId(), seanceDTO.getDateSeance(), seanceDTO.getHeureFin(), seanceDTO.getHeureDebut());
        if (!teacherConflicts.isEmpty()) {
            throw new ConflictException("Conflit d'horaire : L'enseignant est déjà occupé à ce créneau.");
        }

        // Vérifier les conflits pour la salle
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

        seance.setEnseignantId(seanceDTO.getEnseignantId());

        Salle salle = salleRepository.findById(seanceDTO.getSalleId())
                .orElseThrow(() -> new EntityNotFoundException("Salle non trouvée avec l'id: " + seanceDTO.getSalleId()));
        seance.setSalle(salle);

        seance.setEcId(seanceDTO.getEcId());

        return seanceRepository.save(seance);
    }

    public Seance updateSeance(Long id, SeanceDTO seanceDTO) {
        Seance existingSeance = seanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Séance non trouvée avec l'id: " + id));

        // Vérifier les conflits pour l'enseignant
        List<Seance> teacherConflicts = seanceRepository.findByEnseignantIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfterAndIdNot(
                seanceDTO.getEnseignantId(), seanceDTO.getDateSeance(), seanceDTO.getHeureFin(), seanceDTO.getHeureDebut(), id);
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

        existingSeance.setEnseignantId(seanceDTO.getEnseignantId());

        Salle salle = salleRepository.findById(seanceDTO.getSalleId())
                .orElseThrow(() -> new EntityNotFoundException("Salle non trouvée avec l'id: " + seanceDTO.getSalleId()));
        existingSeance.setSalle(salle);

        existingSeance.setEcId(seanceDTO.getEcId());

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
}
