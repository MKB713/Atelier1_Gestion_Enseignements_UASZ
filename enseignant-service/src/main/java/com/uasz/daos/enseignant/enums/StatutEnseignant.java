package com.uasz.daos.enseignant.enums;

public enum StatutEnseignant {
    ACTIF("Actif"),
    INACTIF("Inactif"),
    ARCHIVE("ArchivÃ©");

    private final String displayName;

    StatutEnseignant(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
