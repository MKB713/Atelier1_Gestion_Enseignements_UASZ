package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import jakarta.persistence.*;
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
}
