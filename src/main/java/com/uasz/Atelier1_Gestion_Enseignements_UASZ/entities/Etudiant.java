package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutEtudiant;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "etudiants")
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le matricule est obligatoire")
    @Column(unique = true, nullable = false, length = 20)
    private String matricule;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false, length = 100)
    private String prenom;

    @Email(message = "Email invalide")
    @Column(unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String telephone;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance", length = 100)
    private String lieuNaissance;

    @Column(length = 10)
    private String sexe; // M ou F

    @Column(length = 200)
    private String adresse;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "numero_cni", length = 30)
    private String numeroCNI;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutEtudiant statut = StatutEtudiant.ACTIF;

    @Column(name = "date_inscription")
    private LocalDate dateInscription;

    // Relations
    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscription> inscriptions = new ArrayList<>();

    // Constructeurs
    public Etudiant() {
        this.dateInscription = LocalDate.now();
    }

    public Etudiant(String matricule, String nom, String prenom, String email) {
        this();
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    // Méthodes utilitaires
    public String getNomComplet() {
        return this.prenom + " " + this.nom;
    }

    public int getAge() {
        if (this.dateNaissance != null) {
            return LocalDate.now().getYear() - this.dateNaissance.getYear();
        }
        return 0;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
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

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getNumeroCNI() {
        return numeroCNI;
    }

    public void setNumeroCNI(String numeroCNI) {
        this.numeroCNI = numeroCNI;
    }

    public StatutEtudiant getStatut() {
        return statut;
    }

    public void setStatut(StatutEtudiant statut) {
        this.statut = statut;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public List<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }
}
