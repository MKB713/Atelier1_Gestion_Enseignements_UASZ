package com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto;

import java.util.Map;

public class DashboardStatsDTO {
    // --- TOTAUX ---
    private long totalEnseignants;
    private long totalFormations;
    private long totalClasses;
    private long totalUes;
    private long totalEcs;      // Ajouté
    private long totalFilieres; // Ajouté

    // --- DONNÉES GRAPHIQUES ---
    private Map<String, Long> repartitionParGrade;
    private Map<String, Long> repartitionParFiliere;    // Nouveau
    private Map<String, Long> volumeHoraireGlobal;      // Nouveau

    public DashboardStatsDTO() {}

    // --- GETTERS & SETTERS ---

    public long getTotalEnseignants() { return totalEnseignants; }
    public void setTotalEnseignants(long totalEnseignants) { this.totalEnseignants = totalEnseignants; }

    public long getTotalFormations() { return totalFormations; }
    public void setTotalFormations(long totalFormations) { this.totalFormations = totalFormations; }

    public long getTotalClasses() { return totalClasses; }
    public void setTotalClasses(long totalClasses) { this.totalClasses = totalClasses; }

    public long getTotalUes() { return totalUes; }
    public void setTotalUes(long totalUes) { this.totalUes = totalUes; }

    // CORRECTION ICI : Ajout de 'public'
    public long getTotalEcs() { return totalEcs; }
    public void setTotalEcs(long totalEcs) { this.totalEcs = totalEcs; }

    public long getTotalFilieres() { return totalFilieres; }
    public void setTotalFilieres(long totalFilieres) { this.totalFilieres = totalFilieres; }

    public Map<String, Long> getRepartitionParGrade() { return repartitionParGrade; }
    public void setRepartitionParGrade(Map<String, Long> repartitionParGrade) { this.repartitionParGrade = repartitionParGrade; }

    public Map<String, Long> getRepartitionParFiliere() { return repartitionParFiliere; }
    public void setRepartitionParFiliere(Map<String, Long> repartitionParFiliere) { this.repartitionParFiliere = repartitionParFiliere; }

    public Map<String, Long> getVolumeHoraireGlobal() { return volumeHoraireGlobal; }
    public void setVolumeHoraireGlobal(Map<String, Long> volumeHoraireGlobal) { this.volumeHoraireGlobal = volumeHoraireGlobal; }
}