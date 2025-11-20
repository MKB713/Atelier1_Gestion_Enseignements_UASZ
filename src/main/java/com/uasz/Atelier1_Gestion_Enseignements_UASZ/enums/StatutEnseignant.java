package com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums;

public enum StatutEnseignant {

    // Définition des constantes avec leur valeur d'affichage associée
    ACTIF("Actif"),
    INACTIF("Inactif"),
    ARCHIVE("Archivé");

    // Champ privé pour stocker le nom d'affichage lisible
    private final String displayName;

    /**
     * Constructeur pour initialiser la valeur d'affichage.
     */
    StatutEnseignant(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter pour que Thymeleaf puisse accéder à la propriété via Spring EL:
     * Exemple: ${enseignant.statutEnseignant.displayName}
     * @return Le nom d'affichage de l'état.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Optionnel: Surcharge de toString pour un affichage lisible si l'objet est casté en String.
     */
    @Override
    public String toString() {
        return displayName;
    }
}