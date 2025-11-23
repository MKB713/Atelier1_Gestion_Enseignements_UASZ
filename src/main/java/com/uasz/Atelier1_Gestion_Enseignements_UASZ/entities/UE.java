package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code; // Ex: UE-INFO-L1

    private String libelle; // Ex: Génie Logiciel

    private int credit;
    private int coefficient;

    private Date dateCreation;

    // États (Booléens)
    private boolean active = true;
    private boolean archive = false;
}