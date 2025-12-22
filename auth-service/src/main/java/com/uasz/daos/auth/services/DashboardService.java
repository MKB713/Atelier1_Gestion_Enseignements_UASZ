package com.uasz.daos.auth.services;

import com.uasz.daos.auth.dto.DashboardStatsDTO;
import com.uasz.daos.auth.repository.UtilisateurRepository;
import com.uasz.daos.auth.repository.EnseignantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UtilisateurRepository utilisateurRepository;
    private final EnseignantRepository enseignantRepository;

    public DashboardStatsDTO getStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalUtilisateurs(utilisateurRepository.count());
        stats.setTotalEnseignants(enseignantRepository.count());
        // Les autres stats peuvent être ajoutées quand les microservices correspondants seront disponibles
        stats.setTotalFormations(0);
        stats.setTotalEtudiants(0);
        return stats;
    }
}
