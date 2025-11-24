package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Cycle;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Niveau;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private String libelle;

    @Enumerated(EnumType.STRING)
    private Cycle cycle;

    @Enumerated(EnumType.STRING)
    private Niveau niveau;

    private boolean archive = false;

    // --- C'EST CE CHAMP QUI MANQUAIT DANS VOTRE ERREUR ---
    @ManyToOne
    @JoinColumn(name = "ue_id")
    private UE ue;
    // -----------------------------------------------------

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EC> ecs;
}