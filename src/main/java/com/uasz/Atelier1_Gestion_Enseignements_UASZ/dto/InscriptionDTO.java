package com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutInscription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class InscriptionDTO {

    private Long id;

    @NotNull(message = "L'étudiant est obligatoire")
    private Long etudiantId;

    private Long classeId;
    private Long formationId;

    @NotBlank(message = "L'année académique est obligatoire")
    private String anneeAcademique;

    private Integer anneeFormation;
    private LocalDate dateInscription;
    private StatutInscription statut;
    private Boolean estRedoublant;
    private String remarques;

    // Constructeurs
    public InscriptionDTO() {}

    public InscriptionDTO(Long etudiantId, Long classeId, String anneeAcademique) {
        this.etudiantId = etudiantId;
        this.classeId = classeId;
        this.anneeAcademique = anneeAcademique;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public Long getClasseId() {
        return classeId;
    }

    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }

    public Long getFormationId() {
        return formationId;
    }

    public void setFormationId(Long formationId) {
        this.formationId = formationId;
    }

    public String getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(String anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public Integer getAnneeFormation() {
        return anneeFormation;
    }

    public void setAnneeFormation(Integer anneeFormation) {
        this.anneeFormation = anneeFormation;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public StatutInscription getStatut() {
        return statut;
    }

    public void setStatut(StatutInscription statut) {
        this.statut = statut;
    }

    public Boolean getEstRedoublant() {
        return estRedoublant;
    }

    public void setEstRedoublant(Boolean estRedoublant) {
        this.estRedoublant = estRedoublant;
    }

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }
}
