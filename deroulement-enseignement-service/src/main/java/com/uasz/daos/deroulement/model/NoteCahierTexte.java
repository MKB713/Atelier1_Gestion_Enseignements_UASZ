package com.uasz.daos.deroulement.model;

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

    // Relation externe vers emploi-temps-service
    @Column(name = "seance_id", nullable = false)
    private Long seanceId;

    @Column(length = 500)
    private String objectifsPedagogiques;

    @Column(length = 500)
    private String activitesRealisees;

    @Column(length = 500)
    private String travailDemande;

    @Column(length = 500)
    private String observations;

    // Relation externe vers enseignant-service
    @Column(name = "enseignant_id")
    private Long enseignantId;

    private boolean estValide = false;

    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    public NoteCahierTexte() {
    }

    public NoteCahierTexte(String titre, String contenu, Long seanceId, Long enseignantId) {
        this.titre = titre;
        this.contenu = contenu;
        this.seanceId = seanceId;
        this.enseignantId = enseignantId;
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

    public Long getSeanceId() {
        return seanceId;
    }

    public void setSeanceId(Long seanceId) {
        this.seanceId = seanceId;
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

    public Long getEnseignantId() {
        return enseignantId;
    }

    public void setEnseignantId(Long enseignantId) {
        this.enseignantId = enseignantId;
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
                ", seanceId=" + seanceId +
                ", estValide=" + estValide +
                '}';
    }
}
