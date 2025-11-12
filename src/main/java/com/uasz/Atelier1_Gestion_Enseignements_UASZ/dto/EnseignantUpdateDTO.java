package com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Statut;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class EnseignantUpdateDTO {

    private String grade;

    private Statut statut;

    @Email(message = "L'email doit être valide")
    private String email;

    @Pattern(regexp = "^[+]?[0-9\\s\\-\\(\\)]{7,20}$",
            message = "Le numéro de téléphone doit être valide (7 à 20 caractères, peut contenir +, espaces, tirets, parenthèses)")
    private String telephone;

    private String adresse;

    public EnseignantUpdateDTO() {
    }

    public EnseignantUpdateDTO(String grade, Statut statut, String email, String telephone, String adresse) {
        this.grade = grade;
        this.statut = statut;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}

