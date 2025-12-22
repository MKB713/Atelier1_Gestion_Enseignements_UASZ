package com.uasz.daos.maquette.exceptions;

public class MatriculeAlreadyExistsException extends RuntimeException {
    public MatriculeAlreadyExistsException(String message) {
        super(message);
    }
}
