package com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Enseignant")
public enum Statut {
    PERMANENT, VACATAIRE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    private Long id;
}