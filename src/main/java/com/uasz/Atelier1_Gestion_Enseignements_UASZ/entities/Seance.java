package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateSeance;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private int duree;

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

    @ManyToOne
    @JoinColumn(name = "emploi_id")
    private Emploi emploi;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "repartition_id", referencedColumnName = "id")
    private Repartition repartition;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "deroulement_id", referencedColumnName = "id")
    private Deroulement deroulement;
}
