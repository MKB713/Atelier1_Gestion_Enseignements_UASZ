package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "seances")
public class Seance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDate dateSeance;

    @Column(nullable = false)
    private LocalTime heureDebut;

    @Column(nullable = false)
    private LocalTime heureFin;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;

    @ManyToOne
    @JoinColumn(name = "enseignant_id")
    private Enseignant enseignant;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    private String salle;

    private String typeSeance; // CM, TD, TP, etc.

    private boolean estTerminee = false;

    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    public Seance() {
    }

    public Seance(String titre, String description, LocalDate dateSeance, LocalTime heureDebut,
                  LocalTime heureFin, Classe classe, Enseignant enseignant, Module module) {
        this.titre = titre;
        this.description = description;
        this.dateSeance = dateSeance;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.classe = classe;
        this.enseignant = enseignant;
        this.module = module;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateSeance() {
        return dateSeance;
    }

    public void setDateSeance(LocalDate dateSeance) {
        this.dateSeance = dateSeance;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public String getTypeSeance() {
        return typeSeance;
    }

    public void setTypeSeance(String typeSeance) {
        this.typeSeance = typeSeance;
    }

    public boolean isEstTerminee() {
        return estTerminee;
    }

    public void setEstTerminee(boolean estTerminee) {
        this.estTerminee = estTerminee;
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
        return "Seance{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", dateSeance=" + dateSeance +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", typeSeance='" + typeSeance + '\'' +
                '}';
    }
}
