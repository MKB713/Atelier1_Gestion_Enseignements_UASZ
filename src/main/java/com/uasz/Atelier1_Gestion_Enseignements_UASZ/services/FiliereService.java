package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Filiere;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.FiliereRepository;
import  org.springframework.beans.factory.annotation.Autowired;
public class FiliereService {
    @Autowired
    private FiliereRepository filiereRepository;
    public void addFiliere(Filiere filiere) {
        filiereRepository.save(filiere);

    }
}
