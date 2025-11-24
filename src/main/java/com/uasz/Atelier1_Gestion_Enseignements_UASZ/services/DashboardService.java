package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.DashboardStatsDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Formation;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.EnseignantRepository;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.FiliereRepository;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.FormationRepository;
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

    @Autowired
    private FormationRepository formationRepository; // Nouveau : On connecte les formations

    @Autowired
    private FiliereRepository filiereRepository;     // Nouveau : On connecte les filières

    public DashboardStatsDTO getStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        // =================================================================
        // 1. DONNÉES RÉELLES (Ce qui est déjà codé)
        // =================================================================

        // --- ENSEIGNANTS ---
        List<Enseignant> enseignants = enseignantRepository.findAll();
        stats.setTotalEnseignants(enseignants.size());

        // Graphique 1 : Répartition par Grade (Réel)
        Map<String, Long> parGrade = enseignants.stream()
                .filter(e -> e.getGrade() != null)
                .collect(Collectors.groupingBy(Enseignant::getGrade, Collectors.counting()));
        stats.setRepartitionParGrade(parGrade);

        // --- FORMATIONS ---
        List<Formation> formations = formationRepository.findAll();
        stats.setTotalFormations(formations.size()); // Compte réel

        // Graphique 2 : Formations par Filière (Réel)
        // On groupe les formations par le libellé de leur filière
        Map<String, Long> parFiliere = formations.stream()
                .filter(f -> f.getFiliere() != null)
                .collect(Collectors.groupingBy(f -> f.getFiliere().getLibelle(), Collectors.counting()));

        // Si aucune formation n'a de filière, on met des données par défaut pour ne pas avoir un graphique vide
        if (parFiliere.isEmpty()) {
            parFiliere.put("Aucune donnée", 0L);
        }
        stats.setRepartitionParFiliere(parFiliere);

        // --- FILIERES ---
        stats.setTotalFilieres(filiereRepository.count()); // Compte réel

        // =================================================================
        // 2. DONNÉES SIMULÉES (En attendant votre équipe - Sprint 2)
        // =================================================================

        // Ces tables n'existent pas encore, donc on garde les mocks
        stats.setTotalClasses(24);
        stats.setTotalUes(140);
        stats.setTotalEcs(320);

        // Graphique 3 : Volume Horaire (Simulé pour l'instant)
        Map<String, Long> volumeH = new HashMap<>();
        volumeH.put("CM", 1250L);
        volumeH.put("TD", 850L);
        volumeH.put("TP", 600L);
        stats.setVolumeHoraireGlobal(volumeH);

        return stats;
    }
}