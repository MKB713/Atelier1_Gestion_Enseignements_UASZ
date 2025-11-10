package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import jakarta.persistence.*;
import jakarta.persistence.Basic;
import jakarta.persistence.FetchType;

@Entity
@Table(name = "modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String code;
    @ManyToOne
    private Ec ec;
    @ManyToOne
    private Maquette maquette;
    @ManyToOne
    private Semestre semestre;

    public Module() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Ec getEc() {
        return ec;
    }

    public void setEc(Ec ec) {
        this.ec = ec;
    }

    public Maquette getMaquette() {
        return maquette;
    }

    public void setMaquette(Maquette maquette) {
        this.maquette = maquette;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }
}
