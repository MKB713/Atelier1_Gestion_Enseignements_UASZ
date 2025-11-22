package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutInscription;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inscriptions")
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation avec l'étudiant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    // Relation avec la classe
    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;

    // Relation avec la formation
    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation;

    @Column(name = "annee_academique", nullable = false, length = 20)
    private String anneeAcademique; // Ex: "2024-2025"

    @Column(name = "annee_formation")
    private Integer anneeFormation; // 1ère année, 2ème année, etc.

    @Column(name = "date_inscription")
    private LocalDate dateInscription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutInscription statut = StatutInscription.EN_COURS;

    @Column(name = "est_redoublant")
    private Boolean estRedoublant = false;

    @Column(length = 500)
    private String remarques;

    // Constructeurs
    public Inscription() {
        this.dateInscription = LocalDate.now();
    }

    public Inscription(Etudiant etudiant, Classe classe, String anneeAcademique) {
        this();
        this.etudiant = etudiant;
        this.classe = classe;
        this.anneeAcademique = anneeAcademique;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
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
