package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Salle;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.SalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalleService {

    @Autowired
    private SalleRepository salleRepository;

    public List<Salle> getAllSalles() {
        return salleRepository.findAll();
    }

    public Salle getSalleById(Long id) {
        return salleRepository.findById(id).orElse(null);
    }

    public Salle createSalle(Salle salle) {
        return salleRepository.save(salle);
    }

    public Salle updateSalle(Long id, Salle salleDetails) {
        Salle existingSalle = getSalleById(id);
        if (existingSalle != null) {
            existingSalle.setLibelle(salleDetails.getLibelle());
            existingSalle.setCapacite(salleDetails.getCapacite());
            existingSalle.setDescription(salleDetails.getDescription());
            return salleRepository.save(existingSalle);
        }
        return null;
    }

    public void deleteSalle(Long id) {
        salleRepository.deleteById(id);
    }
}
