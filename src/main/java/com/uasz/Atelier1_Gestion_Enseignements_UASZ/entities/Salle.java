package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    private int capacite;
    private String description;
    private String numeroSalle;

    @ManyToOne
    @JoinColumn(name = "batiment_id")
    private Batiment batiment;

    @ManyToOne
    @JoinColumn(name = "batiment_id")
    private Batiment batiment;

    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL)
    private List<Seance> seances;
}
