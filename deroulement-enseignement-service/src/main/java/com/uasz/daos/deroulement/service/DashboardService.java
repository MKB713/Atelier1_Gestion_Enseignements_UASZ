package com.uasz.daos.deroulement.service;

import com.uasz.daos.deroulement.dto.DashboardStatsDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    // Note: Enseignant, Formation et Filiere sont dans d'autres services
    // Pour le moment, nous utilisons des données simulées

    public DashboardStatsDTO getStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        // =================================================================
        // DONNÉES SIMULÉES (En attendant l'intégration avec les autres services)
        // =================================================================

        // --- ENSEIGNANTS ---
        stats.setTotalEnseignants(45);

        // Graphique 1 : Répartition par Grade (Simulé)
        Map<String, Long> parGrade = new HashMap<>();
        parGrade.put("Professeur", 12L);
        parGrade.put("Maître de Conférences", 18L);
        parGrade.put("Assistant", 15L);
        stats.setRepartitionParGrade(parGrade);

        // --- FORMATIONS ---
        stats.setTotalFormations(8);

        // Graphique 2 : Formations par Filière (Simulé)
        Map<String, Long> parFiliere = new HashMap<>();
        parFiliere.put("Informatique", 3L);
        parFiliere.put("Mathématiques", 2L);
        parFiliere.put("Physique", 3L);
        stats.setRepartitionParFiliere(parFiliere);

        // --- FILIERES ---
        stats.setTotalFilieres(6L); // Simulé

        // =================================================================
        // 2. AUTRES DONNÉES SIMULÉES
        // =================================================================

        // Ces données seront intégrées lors des prochains sprints
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
