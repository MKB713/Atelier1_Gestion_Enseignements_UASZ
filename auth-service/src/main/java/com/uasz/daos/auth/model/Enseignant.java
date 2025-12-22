package com.uasz.daos.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "enseignants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enseignant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long matricule;

    private String nom;
    private String prenom;
    private String telephone;

    @Column(unique = true)
    private String email;

    private String grade;
    private LocalDate dateEmbauche;

    private boolean estActif;
    private String specialite;
}
