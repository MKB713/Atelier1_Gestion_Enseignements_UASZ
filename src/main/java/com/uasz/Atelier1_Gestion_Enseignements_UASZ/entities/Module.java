package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String libelle;

    private String description;
    private int semestre; // 1 ou 2

    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation;

    // RELATION AVEC MAQUETTE AJOUTÉE
    @ManyToOne
    @JoinColumn(name = "maquette_id")
    private Maquette maquette;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private List<UE> ues = new ArrayList<>();

    // Constructeurs
    public Module() {}

    public Module(String code, String libelle, Formation formation, int semestre) {
        this.code = code;
        this.libelle = libelle;
        this.formation = formation;
        this.semestre = semestre;
    }

    public Module(String code, String libelle, Formation formation, Maquette maquette, int semestre) {
        this.code = code;
        this.libelle = libelle;
        this.formation = formation;
        this.maquette = maquette;
        this.semestre = semestre;
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
    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }
    public Formation getFormation() { return formation; }
    public void setFormation(Formation formation) { this.formation = formation; }
    public Maquette getMaquette() { return maquette; }
    public void setMaquette(Maquette maquette) { this.maquette = maquette; }
    public List<UE> getUes() { return ues; }
    public void setUes(List<UE> ues) { this.ues = ues; }
}