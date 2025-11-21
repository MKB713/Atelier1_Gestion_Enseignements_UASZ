package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Filiere;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.FiliereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FiliereService {

    @Autowired
    private FiliereRepository filiereRepository;

    /**
     * Récupère toutes les filières
     */
    public List<Filiere> getAllFilieres() {
        return filiereRepository.findAll();
    }

    /**
     * Récupère une filière par son ID
     */
    public Filiere getFiliereById(Long id) {
        return filiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filière non trouvée avec l'ID : " + id));
    }
}
