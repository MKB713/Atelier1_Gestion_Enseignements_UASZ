package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Repartition;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.RepartitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepartitionService {

    @Autowired
    private RepartitionRepository repartitionRepository;

    public List<Repartition> getAllRepartitions() {
        return repartitionRepository.findAll();
    }

    public Repartition getRepartitionById(Long id) {
        return repartitionRepository.findById(id).orElse(null);
    }

    // Add other CRUD methods as needed...
}
