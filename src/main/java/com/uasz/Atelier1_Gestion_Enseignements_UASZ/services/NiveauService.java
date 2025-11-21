package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Niveau;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.NiveauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NiveauService {

    @Autowired
    private NiveauRepository niveauRepository;

    /**
     * Récupère tous les niveaux
     */
    public List<Niveau> getAllNiveaux() {
        return niveauRepository.findAll();
    }

    /**
     * Récupère un niveau par son ID
     */
    public Niveau getNiveauById(Long id) {
        return niveauRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Niveau non trouvé avec l'ID : " + id));
    }
}
