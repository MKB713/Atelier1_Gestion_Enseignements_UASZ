package com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SeanceDTO {
    private Long id;
    private LocalDate dateSeance;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private int duree;

    // IDs for relationships
    private Long salleId;
    private Long emploiId;
    private Long repartitionId;
    private Long deroulementId;

    // Fields for display
    private String salleNom;
    private String emploiLibelle;
    private String enseignantNom;
    private String ecLibelle;
    private String batimentNom;
}
