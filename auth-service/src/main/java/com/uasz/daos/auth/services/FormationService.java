package com.uasz.daos.auth.services;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class FormationService {

    public List<Object> getAllFormations() {
        // Stub - retourne une liste vide pour l'instant
        // Cette méthode devra être implémentée avec un client Feign vers le microservice maquette
        return new ArrayList<>();
    }
}
