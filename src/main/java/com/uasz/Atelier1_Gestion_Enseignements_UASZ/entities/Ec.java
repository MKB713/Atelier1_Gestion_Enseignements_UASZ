package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ec")
public class EC {
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
    private int volumeHoraireTP;
    private int volumeHoraireTD;

    @ManyToOne
    @JoinColumn(name = "ue_id")
    private UE ue;

    @Column(updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }

    // Constructeurs, Getters/Setters...
    public EC() {}

    public EC(String code, String libelle, UE ue) {
        this.code = code;
        this.libelle = libelle;
        this.ue = ue;
    }

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
    public int getVolumeHoraireTP() { return volumeHoraireTP; }
    public void setVolumeHoraireTP(int volumeHoraireTP) { this.volumeHoraireTP = volumeHoraireTP; }
    public int getVolumeHoraireTD() { return volumeHoraireTD; }
    public void setVolumeHoraireTD(int volumeHoraireTD) { this.volumeHoraireTD = volumeHoraireTD; }
    public UE getUe() { return ue; }
    public void setUe(UE ue) { this.ue = ue; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}