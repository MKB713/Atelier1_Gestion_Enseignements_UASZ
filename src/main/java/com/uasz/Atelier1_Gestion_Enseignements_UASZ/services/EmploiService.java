package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Emploi;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.EmploiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmploiService {

    @Autowired
    private EmploiRepository emploiRepository;

    public List<Emploi> getAllEmplois() {
        return emploiRepository.findAll();
    }

    public Emploi getEmploiById(Long id) {
        return emploiRepository.findById(id).orElse(null);
    }

    // Add other CRUD methods as needed...
}
