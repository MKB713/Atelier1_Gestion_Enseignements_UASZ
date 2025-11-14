package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;
import jakarta.persistence.*;


@Entity
@Table(name = "ecs")
public class Ec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    private String code;
    @ManyToOne
    private Ue ue;

    public Ec() {
    }

    public Ec(Long id, String libelle, String code, Ue ue) {
        this.id = id;
        this.libelle = libelle;
        this.code = code;
        this.ue = ue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Ue getUe() {
        return ue;
    }

    public void setUe(Ue ue) {
        this.ue = ue;
    }
}
