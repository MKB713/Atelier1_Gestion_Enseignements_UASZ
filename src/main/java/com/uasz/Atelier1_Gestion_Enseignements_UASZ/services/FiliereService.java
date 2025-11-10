package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Filiere;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.FiliereRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FiliereService {
    @Autowired
    private FiliereRepository filiereRepository;

    public List<Filiere> getAllFiliere(){
        return filiereRepository.findAll();
    }

    public void addFiliere(Filiere filiere) {
        filiereRepository.save(filiere);
    }

    public Filiere getFiliereById(Long id) {
        return filiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filière non trouvée avec l'id: " + id));
    }

    public void deleteFiliere(Long id) {
        filiereRepository.deleteById(id);
    }
}
