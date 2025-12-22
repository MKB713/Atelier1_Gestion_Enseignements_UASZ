package com.uasz.daos.auth.enums;

public enum Role {
    ADMIN("Administrateur"),
    RESPONSABLE_MASTER("Responsable Master"),
    CHEF_DE_DEPARTEMENT("Chef de DÃ©partement"),
    ENSEIGNANT("Enseignant"),
    ETUDIANT("Ã‰tudiant"),
    COORDONATEUR_DES_LICENCES("Coordinateur des Licences");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isAdmin() {
        return this == ADMIN || this == CHEF_DE_DEPARTEMENT;
    }
}
