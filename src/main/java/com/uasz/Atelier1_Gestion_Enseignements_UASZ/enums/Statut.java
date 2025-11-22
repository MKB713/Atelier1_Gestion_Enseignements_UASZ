package com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

public enum Statut {
    PERMANENT("Permanent"),
    VACATAIRE("Vacataire");

    private final String displayValue;

    Statut(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}