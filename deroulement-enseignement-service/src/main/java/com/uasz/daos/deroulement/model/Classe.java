package com.uasz.daos.deroulement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "classes")
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String libelle;

    @Column(length = 500)
    private String description;

    // Relation externe vers maquette-service
    @Column(name = "filiere_id")
    private Long filiereId;

    // Relation externe vers maquette-service
    @Column(name = "niveau_id")
    private Long niveauId;

    private String anneeAcademique;

    private Integer effectifMax;

    private boolean estActive = true;

    private boolean estArchivee = false;

    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    public Classe() {
    }

    public Classe(String code, String libelle, String description, Long filiereId, Long niveauId,
                  String anneeAcademique, Integer effectifMax) {
        this.code = code;
        this.libelle = libelle;
        this.description = description;
        this.filiereId = filiereId;
        this.niveauId = niveauId;
        this.anneeAcademique = anneeAcademique;
        this.effectifMax = effectifMax;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getFiliereId() {
        return filiereId;
    }

    public void setFiliereId(Long filiereId) {
        this.filiereId = filiereId;
    }

    public Long getNiveauId() {
        return niveauId;
    }

    public void setNiveauId(Long niveauId) {
        this.niveauId = niveauId;
    }

    public String getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(String anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public Integer getEffectifMax() {
        return effectifMax;
    }

    public void setEffectifMax(Integer effectifMax) {
        this.effectifMax = effectifMax;
    }

    public boolean isEstActive() {
        return estActive;
    }

    public void setEstActive(boolean estActive) {
        this.estActive = estActive;
    }

    public boolean isEstArchivee() {
        return estArchivee;
    }

    public void setEstArchivee(boolean estArchivee) {
        this.estArchivee = estArchivee;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    @Override
    public String toString() {
        return "Classe{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", libelle='" + libelle + '\'' +
                ", description='" + description + '\'' +
                ", anneeAcademique='" + anneeAcademique + '\'' +
                ", effectifMax=" + effectifMax +
                ", estActive=" + estActive +
                ", estArchivee=" + estArchivee +
                '}';
    }
}
