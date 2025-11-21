package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notes_cahier_texte")
public class NoteCahierTexte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, length = 2000)
    private String contenu;

    @ManyToOne
    @JoinColumn(name = "seance_id", nullable = false)
    private Seance seance;

    @Column(length = 500)
    private String objectifsPedagogiques;

    @Column(length = 500)
    private String activitesRealisees;

    @Column(length = 500)
    private String travailDemande;

    @Column(length = 500)
    private String observations;

    @ManyToOne
    @JoinColumn(name = "enseignant_id")
    private Enseignant enseignant;

    private boolean estValide = false;

    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    public NoteCahierTexte() {
    }

    public NoteCahierTexte(String titre, String contenu, Seance seance, Enseignant enseignant) {
        this.titre = titre;
        this.contenu = contenu;
        this.seance = seance;
        this.enseignant = enseignant;
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

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    public String getObjectifsPedagogiques() {
        return objectifsPedagogiques;
    }

    public void setObjectifsPedagogiques(String objectifsPedagogiques) {
        this.objectifsPedagogiques = objectifsPedagogiques;
    }

    public String getActivitesRealisees() {
        return activitesRealisees;
    }

    public void setActivitesRealisees(String activitesRealisees) {
        this.activitesRealisees = activitesRealisees;
    }

    public String getTravailDemande() {
        return travailDemande;
    }

    public void setTravailDemande(String travailDemande) {
        this.travailDemande = travailDemande;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public boolean isEstValide() {
        return estValide;
    }

    public void setEstValide(boolean estValide) {
        this.estValide = estValide;
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
        return "NoteCahierTexte{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", seance=" + (seance != null ? seance.getId() : null) +
                ", estValide=" + estValide +
                '}';
    }
}
