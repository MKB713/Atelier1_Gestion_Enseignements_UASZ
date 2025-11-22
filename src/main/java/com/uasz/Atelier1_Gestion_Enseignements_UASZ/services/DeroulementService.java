package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Deroulement;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.DeroulementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeroulementService {

    @Autowired
    private DeroulementRepository deroulementRepository;

    public List<Deroulement> getAllDeroulements() {
        return deroulementRepository.findAll();
    }

    public Deroulement getDeroulementById(Long id) {
        return deroulementRepository.findById(id).orElse(null);
    }

    // Add other CRUD methods as needed...
}
