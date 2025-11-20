package com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Statut;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class EnseignantUpdateDTO {

    // --- Champs d'identité/spécialité (Maintenant modifiables) ---
    // Ces champs ont été réintroduits pour correspondre aux modifications du HTML/Service
    private String nom;
    private String prenom;
    private String specialite;

    @NotNull(message = "La date d'embauche ne peut pas être nulle")
    private LocalDate dateEmbauche;

    // --- Champs de contact/grade (Existants) ---
    private String grade;
    private Statut statut;

    @Email(message = "L'email doit être valide")
    private String email;

    @Pattern(regexp = "^[+]?[0-9\\s\\-\\(\\)]{7,20}$",
            message = "Le numéro de téléphone doit être valide (7 à 20 caractères, peut contenir +, espaces, tirets, parenthèses)")
    private String telephone;

    private String adresse;

    // --------------------------------------------------------------------------------
    // --- CONSTRUCTEURS ---
    // --------------------------------------------------------------------------------

    // Constructeur sans argument
    public EnseignantUpdateDTO() {
    }

    // Constructeur complet (avec tous les 9 champs)
    public EnseignantUpdateDTO(String nom, String prenom, String specialite, LocalDate dateEmbauche, String grade, Statut statut, String email, String telephone, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.dateEmbauche = dateEmbauche;
        this.grade = grade;
        this.statut = statut;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
    }

    // --------------------------------------------------------------------------------
    // --- GETTERS & SETTERS (Incluant les champs réintégrés) ---
    // --------------------------------------------------------------------------------

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    // Champs existants :
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
}