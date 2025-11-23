package com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums;

public enum StatutChoix {
    EN_ATTENTE("En attente"),
    VALIDE("Validé"),
    REJETE("Rejeté");

    private final String displayValue;

    StatutChoix(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
