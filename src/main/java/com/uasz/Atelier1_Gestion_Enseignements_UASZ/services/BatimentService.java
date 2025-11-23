package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Batiment;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.BatimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BatimentService {

    @Autowired
    private BatimentRepository batimentRepository;

    public List<Batiment> getAllBatiments() {
        return batimentRepository.findAll();
    }

    public Optional<Batiment> getBatimentById(Long id) {
        return batimentRepository.findById(id);
    }

    public Batiment saveBatiment(Batiment batiment) {
        return batimentRepository.save(batiment);
    }

    public void deleteBatiment(Long id) {
        batimentRepository.deleteById(id);
    }
}
