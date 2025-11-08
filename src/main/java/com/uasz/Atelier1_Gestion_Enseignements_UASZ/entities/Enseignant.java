package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Statut;
import jakarta.persistence.*;

import java.util.Date;

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
    private String DateNaissance;
    private Date LieuNaissance;
    @Enumerated(EnumType.STRING)
    private Statut statut;
    private boolean estActif;

    public Enseignant() {
    }

    public Enseignant(Long id, Long matricule, String nom, String prenom, String adresse, String telephone, String email, String grade, String dateNaissance, Date lieuNaissance, Statut statut, boolean estActif) {
        this.id = id;
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.grade = grade;
        DateNaissance = dateNaissance;
        LieuNaissance = lieuNaissance;
        this.statut = statut;
        this.estActif = estActif;
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

    public String getDateNaissance() {
        return DateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        DateNaissance = dateNaissance;
    }

    public Date getLieuNaissance() {
        return LieuNaissance;
    }

    public void setLieuNaissance(Date lieuNaissance) {
        LieuNaissance = lieuNaissance;
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
}