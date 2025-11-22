package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "maquettes")
public class Maquette {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String libelle;

    private String description;
    private int anneeAcademique; // Ex: 2025

    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation ;

    // RELATION MAINTAINED - maintenant elle fonctionnera car Module a le champ maquette
    @OneToMany(mappedBy = "maquette", cascade = CascadeType.ALL)
    private List<Module> modules = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }

    // Constructeurs
    public Maquette() {}

    public Maquette(String libelle, Formation formation, int anneeAcademique) {
        this.libelle = libelle;
        this.formation = formation;
        this.anneeAcademique = anneeAcademique;
    }

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getAnneeAcademique() { return anneeAcademique; }
    public void setAnneeAcademique(int anneeAcademique) { this.anneeAcademique = anneeAcademique; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Formation getFormation() { return formation; }
    public void setFormation(Formation formation) { this.formation = formation; }
    public List<Module> getModules() { return modules; }
    public void setModules(List<Module> modules) { this.modules = modules; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}