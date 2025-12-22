package com.uasz.daos.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalFormations;
    private long totalEnseignants;
    private long totalEtudiants;
    private long totalUtilisateurs;
}
