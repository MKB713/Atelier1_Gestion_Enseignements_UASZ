package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.SeanceDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.EC;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Salle;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.exceptions.ConflictException;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.ECRepository;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.EnseignantRepository;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.SalleRepository;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.SeanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeanceService {

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private SalleRepository salleRepository;

    @Autowired
    private ECRepository ecRepository;

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

        Enseignant enseignant = enseignantRepository.findById(seanceDTO.getEnseignantId())
                .orElseThrow(() -> new EntityNotFoundException("Enseignant non trouvé avec l'id: " + seanceDTO.getEnseignantId()));
        seance.setEnseignant(enseignant);

        Salle salle = salleRepository.findById(seanceDTO.getSalleId())
                .orElseThrow(() -> new EntityNotFoundException("Salle non trouvée avec l'id: " + seanceDTO.getSalleId()));
        seance.setSalle(salle);

        EC ec = ecRepository.findById(seanceDTO.getEcId())
                .orElseThrow(() -> new EntityNotFoundException("EC non trouvé avec l'id: " + seanceDTO.getEcId()));
        seance.setEc(ec);

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

        Enseignant enseignant = enseignantRepository.findById(seanceDTO.getEnseignantId())
                .orElseThrow(() -> new EntityNotFoundException("Enseignant non trouvé avec l'id: " + seanceDTO.getEnseignantId()));
        existingSeance.setEnseignant(enseignant);

        Salle salle = salleRepository.findById(seanceDTO.getSalleId())
                .orElseThrow(() -> new EntityNotFoundException("Salle non trouvée avec l'id: " + seanceDTO.getSalleId()));
        existingSeance.setSalle(salle);

        EC ec = ecRepository.findById(seanceDTO.getEcId())
                .orElseThrow(() -> new EntityNotFoundException("EC non trouvé avec l'id: " + seanceDTO.getEcId()));
        existingSeance.setEc(ec);

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
