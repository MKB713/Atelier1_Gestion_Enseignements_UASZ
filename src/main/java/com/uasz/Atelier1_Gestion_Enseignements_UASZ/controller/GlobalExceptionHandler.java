package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.exceptions.MatriculeAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les erreurs de validation des DTOs (pour les endpoints REST/API)
     * Levée par @Valid lorsqu'un champ est invalide.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Gère les erreurs de logique métier (ex: Email déjà utilisé, ID non trouvé)
     * pour les endpoints REST/API.
     * Cette méthode capture l'exception IllegalArgumentException levée dans votre EnseignantService.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Retourne un statut 400 BAD REQUEST avec le message d'erreur
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Gère spécifiquement l'exception de matricule (même si moins utilisée maintenant en création,
     * elle est toujours pertinente pour l'intégrité ou d'autres usages futurs).
     */
    @ExceptionHandler(MatriculeAlreadyExistsException.class)
    public ResponseEntity<String> handleMatriculeAlreadyExistsException(MatriculeAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}