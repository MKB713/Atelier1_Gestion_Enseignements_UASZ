package com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NoteCahierTexteDTO {

    @NotBlank(message = "Le titre ne peut pas être vide")
    @Size(max = 255, message = "Le titre ne peut pas dépasser 255 caractères")
    private String titre;

    @NotBlank(message = "Le contenu ne peut pas être vide")
    @Size(max = 2000, message = "Le contenu ne peut pas dépasser 2000 caractères")
    private String contenu;

    @NotNull(message = "La séance doit être sélectionnée")
    private Long seanceId;

    @Size(max = 500, message = "Les objectifs pédagogiques ne peuvent pas dépasser 500 caractères")
    private String objectifsPedagogiques;

    @Size(max = 500, message = "Les activités réalisées ne peuvent pas dépasser 500 caractères")
    private String activitesRealisees;

    @Size(max = 500, message = "Le travail demandé ne peut pas dépasser 500 caractères")
    private String travailDemande;

    @Size(max = 500, message = "Les observations ne peuvent pas dépasser 500 caractères")
    private String observations;

    private Long enseignantId;

    public NoteCahierTexteDTO() {
    }

    public NoteCahierTexteDTO(String titre, String contenu, Long seanceId) {
        this.titre = titre;
        this.contenu = contenu;
        this.seanceId = seanceId;
    }

    // Getters and Setters

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

    @Override
    public String toString() {
        return "NoteCahierTexteDTO{" +
                "titre='" + titre + '\'' +
                ", seanceId=" + seanceId +
                ", enseignantId=" + enseignantId +
                '}';
    }
}
