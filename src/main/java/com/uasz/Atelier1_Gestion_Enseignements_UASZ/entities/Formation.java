package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Formations")
public class Formation {
    @Id
    private Long id;
    private String code;
    private String libelle;
    private String description;
    private Date dateCreation;
    @ManyToOne
    private Filiere filiere;
    @ManyToOne
    private Niveau niveau;
    @OneToOne(mappedBy = "formation")
    private Maquette maquette;

    public Formation() {
    }

    public Formation(Long id, String code, String libelle, String description, Date dateCreation, Filiere filiere, Niveau niveau) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.description = description;
        this.dateCreation = dateCreation;
        this.filiere = filiere;
        this.niveau = niveau;
    }

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

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }
}


