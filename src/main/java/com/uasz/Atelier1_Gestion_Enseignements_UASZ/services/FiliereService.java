package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Filiere;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.FiliereRepository;
import org.apache.catalina.LifecycleState;
import java.util.List;
import  org.springframework.beans.factory.annotation.Autowired;
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
}
