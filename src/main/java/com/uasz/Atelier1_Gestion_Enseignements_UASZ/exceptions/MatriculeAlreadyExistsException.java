package com.uasz.Atelier1_Gestion_Enseignements_UASZ.exceptions;

public class MatriculeAlreadyExistsException extends RuntimeException {
    public MatriculeAlreadyExistsException(String message) {
        super(message);
    }
}
