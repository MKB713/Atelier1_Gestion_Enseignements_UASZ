package com.uasz.daos.enseignant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "coordinateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordinateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Le prÃ©nom est obligatoire")
    @Column(nullable = false, length = 100)
    private String prenom;

    @Email(message = "Email invalide")
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(length = 20)
    private String telephone;

    // Relation avec Formation via ID (microservice maquette-service)
    @Column(name = "formation_id")
    private Long formationId;

    // Relation avec Enseignant dans le mÃªme service
    @Column(name = "enseignant_id")
    private Long enseignantId;

    @Column(name = "date_debut_fonction")
    private LocalDate dateDebutFonction;

    @Column(name = "date_fin_fonction")
    private LocalDate dateFinFonction;

    @Column(name = "actif")
    private Boolean actif = true;

    @Column(length = 500)
    private String remarques;

    public String getNomComplet() {
        return this.prenom + " " + this.nom;
    }

    public boolean estActif() {
        if (this.dateFinFonction == null) {
            return this.actif;
        }
        return this.actif && LocalDate.now().isBefore(this.dateFinFonction);
    }
}
