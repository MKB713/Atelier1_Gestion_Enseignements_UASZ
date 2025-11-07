package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import org.hibernate.annotations.Table;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "semestres")
public class Semestre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;

    public Semestre() {}

}
