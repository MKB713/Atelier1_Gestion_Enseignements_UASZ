package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutFormation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "formations")
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    @Column(unique = true)
    private String libelle;

    private String description;
    private Date dateCreation;

    @ManyToOne
    @JoinColumn(name = "filiere_id")
    private Filiere filiere;

    @ManyToOne
    @JoinColumn(name = "niveau_id")
    private Niveau niveau;

    // --- RETOUR A LA MÉTHODE STANDARD (Booleans) ---
    private boolean active = true;  // Pour Activer/Désactiver (Vert/Rouge)
    private boolean archive = false; // Pour la corbeille

    // On garde l'enum si vous l'utilisez ailleurs, mais on ne s'en sert plus pour l'activation
    @Enumerated(EnumType.STRING)
    private StatutFormation statutFormation = StatutFormation.ACTIVE;
}