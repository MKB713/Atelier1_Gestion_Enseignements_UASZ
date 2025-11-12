package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.EnseignantUpdateDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EnseignantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enseignants")
public class EnseignantRestController {

    @Autowired
    private EnseignantService enseignantService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEnseignant(@PathVariable Long id,
                                              @Valid @RequestBody EnseignantUpdateDTO updateDTO) {
        try {
            Enseignant updatedEnseignant = enseignantService.updateEnseignant(id, updateDTO);
            return ResponseEntity.ok(updatedEnseignant);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur de validation: " + e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("non trouvé")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Enseignant non trouvé avec l'id: " + id);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}

