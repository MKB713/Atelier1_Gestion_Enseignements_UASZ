package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Statut;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.validation.MinimumAge;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="Enseignants")
public class Enseignant {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long matricule;
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String email;
    private String grade;
    @PastOrPresent(message = "La date d'embauche ne peut pas être dans le futur")
    private LocalDate dateEmbauche;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    @PastOrPresent(message = "La date de naissance ne peut pas être dans le futur")
    @MinimumAge(value = 25, message = "L'enseignant doit avoir au moins 25 ans")
    private LocalDate dateNaissance;
    private String lieuNaissance;
    @Enumerated(EnumType.STRING)
    private Statut statut;
    private boolean estActif;
    private String specialite;

    public Enseignant() {
    }

    public Enseignant(Long id, Long matricule, String nom, String prenom, String adresse, String telephone, String email, String grade, LocalDate dateEmbauche, LocalDateTime dateCreation, LocalDateTime dateModification, LocalDate dateNaissance, String lieuNaissance, Statut statut, boolean estActif, String specialite) {
        this.id = id;
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.grade = grade;
        this.dateEmbauche = dateEmbauche;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.statut = statut;
        this.estActif = estActif;
        this.specialite = specialite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatricule() {
        return matricule;
    }

    public void setMatricule(Long matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public LocalDate getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public boolean isEstActif() {
        return estActif;
    }

    public void setEstActif(boolean estActif) {
        this.estActif = estActif;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
}