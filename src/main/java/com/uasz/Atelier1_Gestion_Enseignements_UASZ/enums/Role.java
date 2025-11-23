package com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums;

public enum Role {
    ETUDIANT("Étudiant"),
    ENSEIGNANT("Enseignant"),
    RESPONSABLE("Responsable Master"),
    COORDINATEUR("Coordinateur Licence"),
    ADMIN("Administrateur");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Vérifie si le rôle a un accès complet (admin)
     */
    public boolean isAdmin() {
        return this == ADMIN || this == RESPONSABLE || this == COORDINATEUR;
    }

    /**
     * Vérifie si le rôle peut modifier les données
     */
    public boolean canEdit() {
        return this != ETUDIANT;
    }

    /**
     * Vérifie si le rôle peut voir le cahier de texte
     */
    public boolean canAccessCahierTexte() {
        return this == ENSEIGNANT || isAdmin();
    }

    /**
     * Vérifie si le rôle peut voir les maquettes
     */
    public boolean canAccessMaquettes() {
        return this == ENSEIGNANT || isAdmin();
    }
}
