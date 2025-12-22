package com.uasz.daos.deroulement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historique_modifications_notes")
public class HistoriqueModificationNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "note_id", nullable = false)
    private NoteCahierTexte note;

    // Relation externe vers enseignant-service
    @Column(name = "enseignant_id")
    private Long enseignantModificateurId;

    @Column(nullable = false)
    private LocalDateTime dateModification;

    @Column(length = 100)
    private String typeModification; // CREATION, MODIFICATION, VALIDATION

    @Column(length = 1000)
    private String ancienneValeur;

    @Column(length = 1000)
    private String nouvelleValeur;

    @Column(length = 255)
    private String champModifie; // titre, contenu, objectifsPedagogiques, etc.

    @Column(length = 500)
    private String commentaire;

    public HistoriqueModificationNote() {
    }

    public HistoriqueModificationNote(NoteCahierTexte note, Long enseignantModificateurId,
                                     String typeModification, String champModifie,
                                     String ancienneValeur, String nouvelleValeur) {
        this.note = note;
        this.enseignantModificateurId = enseignantModificateurId;
        this.dateModification = LocalDateTime.now();
        this.typeModification = typeModification;
        this.champModifie = champModifie;
        this.ancienneValeur = ancienneValeur;
        this.nouvelleValeur = nouvelleValeur;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NoteCahierTexte getNote() {
        return note;
    }

    public void setNote(NoteCahierTexte note) {
        this.note = note;
    }

    public Long getEnseignantModificateurId() {
        return enseignantModificateurId;
    }

    public void setEnseignantModificateurId(Long enseignantModificateurId) {
        this.enseignantModificateurId = enseignantModificateurId;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public String getTypeModification() {
        return typeModification;
    }

    public void setTypeModification(String typeModification) {
        this.typeModification = typeModification;
    }

    public String getAncienneValeur() {
        return ancienneValeur;
    }

    public void setAncienneValeur(String ancienneValeur) {
        this.ancienneValeur = ancienneValeur;
    }

    public String getNouvelleValeur() {
        return nouvelleValeur;
    }

    public void setNouvelleValeur(String nouvelleValeur) {
        this.nouvelleValeur = nouvelleValeur;
    }

    public String getChampModifie() {
        return champModifie;
    }

    public void setChampModifie(String champModifie) {
        this.champModifie = champModifie;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public String toString() {
        return "HistoriqueModificationNote{" +
                "id=" + id +
                ", typeModification='" + typeModification + '\'' +
                ", champModifie='" + champModifie + '\'' +
                ", dateModification=" + dateModification +
                '}';
    }
}
