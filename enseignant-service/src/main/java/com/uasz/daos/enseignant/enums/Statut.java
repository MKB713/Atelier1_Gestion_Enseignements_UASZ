package com.uasz.daos.enseignant.enums;

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
