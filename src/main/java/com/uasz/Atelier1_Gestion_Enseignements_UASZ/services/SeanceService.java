package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.SeanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeanceService {

    @Autowired
    private SeanceRepository seanceRepository;

    /**
     * Récupère toutes les séances
     */
    public List<Seance> getAllSeances() {
        return seanceRepository.findAll();
    }

    /**
     * Récupère une séance par son ID
     */
    public Seance getSeanceById(Long id) {
        return seanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Séance non trouvée avec l'ID : " + id));
    }

    /**
     * Récupère les séances non terminées
     */
    public List<Seance> getSeancesNonTerminees() {
        return seanceRepository.findByEstTerminee(false);
    }
}
