package com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto;

import java.util.Map;

public class DashboardStatsDTO {
    // Totaux
    private long totalEnseignants;
    private long totalFormations;
    private long totalClasses;
    private long totalUes;


    // Données pour les graphiques
    private Map<String, Long> repartitionParGrade;

    // Constructeurs, Getters et Setters
    public DashboardStatsDTO() {}

    public long getTotalEnseignants() { return totalEnseignants; }
    public void setTotalEnseignants(long totalEnseignants) { this.totalEnseignants = totalEnseignants; }

    public long getTotalFormations() { return totalFormations; }
    public void setTotalFormations(long totalFormations) { this.totalFormations = totalFormations; }

    public long getTotalClasses() { return totalClasses; }
    public void setTotalClasses(long totalClasses) { this.totalClasses = totalClasses; }

    public long getTotalUes() { return totalUes; }
    public void setTotalUes(long totalUes) { this.totalUes = totalUes; }

    public Map<String, Long> getRepartitionParGrade() { return repartitionParGrade; }
    public void setRepartitionParGrade(Map<String, Long> repartitionParGrade) { this.repartitionParGrade = repartitionParGrade; }
}