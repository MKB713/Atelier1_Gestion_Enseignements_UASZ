package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.DashboardStatsDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private EnseignantRepository enseignantRepository;

    public DashboardStatsDTO getStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        // 1. VRAIES DONNÉES (Module Enseignants)
        List<Enseignant> enseignants = enseignantRepository.findAll();
        stats.setTotalEnseignants(enseignants.size());

        // Calcul pour le graphique : Groupage par Grade
        Map<String, Long> parGrade = enseignants.stream()
                .filter(e -> e.getGrade() != null)
                .collect(Collectors.groupingBy(Enseignant::getGrade, Collectors.counting()));
        stats.setRepartitionParGrade(parGrade);

        // 2. DONNÉES SIMULÉES (En attendant le Sprint 2 de l'équipe)
        // TODO: Remplacer par formationRepository.count() quand disponible
        stats.setTotalFormations(12);

        // TODO: Remplacer par classeRepository.count() quand disponible
        stats.setTotalClasses(24);

        // TODO: Remplacer par ueRepository.count() quand disponible
        stats.setTotalUes(45);

        return stats;


    }

}