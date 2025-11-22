package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "coordinateurs")
public class Coordinateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false, length = 100)
    private String prenom;

    @Email(message = "Email invalide")
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(length = 20)
    private String telephone;

    // Relation avec Formation (un coordinateur coordonne une formation - généralement licence)
    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation;

    // Optionnel: relation avec Enseignant si le coordinateur est aussi enseignant
    @ManyToOne
    @JoinColumn(name = "enseignant_id")
    private Enseignant enseignant;

    @Column(name = "date_debut_fonction")
    private LocalDate dateDebutFonction;

    @Column(name = "date_fin_fonction")
    private LocalDate dateFinFonction;

    @Column(name = "actif")
    private Boolean actif = true;

    @Column(length = 500)
    private String remarques;

    // Constructeurs
    public Coordinateur() {
        this.dateDebutFonction = LocalDate.now();
    }

    public Coordinateur(String nom, String prenom, String email) {
        this();
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    // Méthodes utilitaires
    public String getNomComplet() {
        return this.prenom + " " + this.nom;
    }

    public boolean estActif() {
        if (this.dateFinFonction == null) {
            return this.actif;
        }
        return this.actif && LocalDate.now().isBefore(this.dateFinFonction);
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public LocalDate getDateDebutFonction() {
        return dateDebutFonction;
    }

    public void setDateDebutFonction(LocalDate dateDebutFonction) {
        this.dateDebutFonction = dateDebutFonction;
    }

    public LocalDate getDateFinFonction() {
        return dateFinFonction;
    }

    public void setDateFinFonction(LocalDate dateFinFonction) {
        this.dateFinFonction = dateFinFonction;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }
}
