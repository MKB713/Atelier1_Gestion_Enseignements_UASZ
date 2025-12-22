package com.uasz.daos.deroulement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnseignantDTO {
    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String grade;
    private String specialite;

    public String getNomComplet() {
        return this.prenom + " " + this.nom;
    }
}
