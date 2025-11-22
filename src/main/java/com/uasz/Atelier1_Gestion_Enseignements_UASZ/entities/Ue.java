package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ues")
public class Ue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String libelle;

    private String description;
    private int credits;
    private int volumeHoraire;

    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation;

    // RELATION AVEC MODULE AJOUTÉE
    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @OneToMany(mappedBy = "ue", cascade = CascadeType.ALL)
    private List<EC> ecs = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }

    // Constructeurs
    public Ue() {}

    public Ue(String code, String libelle, Formation formation) {
        this.code = code;
        this.libelle = libelle;
        this.formation = formation;
    }

    public Ue(String code, String libelle, Formation formation, Module module) {
        this.code = code;
        this.libelle = libelle;
        this.formation = formation;
        this.module = module;
    }

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public int getVolumeHoraire() { return volumeHoraire; }
    public void setVolumeHoraire(int volumeHoraire) { this.volumeHoraire = volumeHoraire; }
    public Formation getFormation() { return formation; }
    public void setFormation(Formation formation) { this.formation = formation; }
    public Module getModule() { return module; }
    public void setModule(Module module) { this.module = module; }
    public List<EC> getEcs() { return ecs; }
    public void setEcs(List<EC> ecs) { this.ecs = ecs; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}