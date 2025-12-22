package com.uasz.daos.enseignant.exceptions;

public class MatriculeAlreadyExistsException extends RuntimeException {
    public MatriculeAlreadyExistsException(String message) {
        super(message);
    }
}
