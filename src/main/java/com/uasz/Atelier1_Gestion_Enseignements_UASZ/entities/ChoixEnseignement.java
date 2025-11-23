package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutChoix;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "choix_enseignements")
public class ChoixEnseignement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enseignant_id", nullable = false)
    private Enseignant enseignant;

    @ManyToOne
    @JoinColumn(name = "ue_id", nullable = false)
    private UE ue;

    @ManyToOne
    @JoinColumn(name = "ec_id", nullable = false)
    private EC ec;

    @Column(nullable = false)
    private String semestre; // S1, S2, etc.

    @Column(nullable = false)
    private Integer volumeHoraire; // en heures

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutChoix statut = StatutChoix.EN_ATTENTE;

    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private String commentaire; // Commentaire du chef de département

    // Constructeurs
    public ChoixEnseignement() {}

    public ChoixEnseignement(Enseignant enseignant, UE ue, EC ec, String semestre, Integer volumeHoraire) {
        this.enseignant = enseignant;
        this.ue = ue;
        this.ec = ec;
        this.semestre = semestre;
        this.volumeHoraire = volumeHoraire;
        this.statut = StatutChoix.EN_ATTENTE;
        this.dateCreation = LocalDateTime.now();
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Enseignant getEnseignant() { return enseignant; }
    public void setEnseignant(Enseignant enseignant) { this.enseignant = enseignant; }

    public UE getUe() { return ue; }
    public void setUe(UE ue) { this.ue = ue; }

    public EC getEc() { return ec; }
    public void setEc(EC ec) { this.ec = ec; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public Integer getVolumeHoraire() { return volumeHoraire; }
    public void setVolumeHoraire(Integer volumeHoraire) { this.volumeHoraire = volumeHoraire; }

    public StatutChoix getStatut() { return statut; }
    public void setStatut(StatutChoix statut) { this.statut = statut; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}