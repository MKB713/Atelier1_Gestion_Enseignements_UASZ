package com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutChoix;
import java.time.LocalDateTime;

public class ChoixEnseignementDTO {
    private Long id;
    private Long enseignantId;
    private String enseignantNom;
    private String enseignantMatricule;
    private Long ueId;
    private String ueLibelle;
    private Long ecId;
    private String ecLibelle;
    private String semestre;
    private Integer volumeHoraire;
    private StatutChoix statut;
    private LocalDateTime dateCreation;
    private String commentaire;

    // Constructeur vide
    public ChoixEnseignementDTO() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEnseignantId() { return enseignantId; }
    public void setEnseignantId(Long enseignantId) { this.enseignantId = enseignantId; }

    public String getEnseignantNom() { return enseignantNom; }
    public void setEnseignantNom(String enseignantNom) { this.enseignantNom = enseignantNom; }

    public String getEnseignantMatricule() { return enseignantMatricule; }
    public void setEnseignantMatricule(String enseignantMatricule) { this.enseignantMatricule = enseignantMatricule; }

    public Long getUeId() { return ueId; }
    public void setUeId(Long ueId) { this.ueId = ueId; }

    public String getUeLibelle() { return ueLibelle; }
    public void setUeLibelle(String ueLibelle) { this.ueLibelle = ueLibelle; }

    public Long getEcId() { return ecId; }
    public void setEcId(Long ecId) { this.ecId = ecId; }

    public String getEcLibelle() { return ecLibelle; }
    public void setEcLibelle(String ecLibelle) { this.ecLibelle = ecLibelle; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public Integer getVolumeHoraire() { return volumeHoraire; }
    public void setVolumeHoraire(Integer volumeHoraire) { this.volumeHoraire = volumeHoraire; }

    public StatutChoix getStatut() { return statut; }
    public void setStatut(StatutChoix statut) { this.statut = statut; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
