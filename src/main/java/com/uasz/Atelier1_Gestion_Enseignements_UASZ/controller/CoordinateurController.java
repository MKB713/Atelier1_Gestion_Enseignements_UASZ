package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.CoordinateurDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Coordinateur;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.CoordinateurService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EnseignantService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FormationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CoordinateurController {

    @Autowired
    private CoordinateurService coordinateurService;

    @Autowired
    private FormationService formationService;

    @Autowired
    private EnseignantService enseignantService;

    /**
     * Affiche la liste de tous les coordinateurs
     */
    @GetMapping("/lst-coordinateurs")
    public String listCoordinateurs(Model model) {
        model.addAttribute("coordinateurs", coordinateurService.getAllCoordinateurs());
        model.addAttribute("coordinateursActifs", coordinateurService.getCoordinateursActifs());
        return "coordinateur-list";
    }

    /**
     * Affiche le formulaire pour ajouter un coordinateur
     */
    @GetMapping("/add-coordinateur")
    public String addCoordinateur(Model model) {
        CoordinateurDTO coordinateurDTO = new CoordinateurDTO();
        model.addAttribute("coordinateur", coordinateurDTO);
        model.addAttribute("formations", formationService.getAllFormations());
        model.addAttribute("enseignants", enseignantService.getAllEnseignants());
        return "coordinateur-add";
    }

    /**
     * Enregistre un nouveau coordinateur
     */
    @PostMapping("/save-coordinateur")
    public String saveCoordinateur(@Valid @ModelAttribute("coordinateur") CoordinateurDTO coordinateurDTO,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("enseignants", enseignantService.getAllEnseignants());
            return "coordinateur-add";
        }

        try {
            coordinateurService.creerCoordinateur(coordinateurDTO);
            return "redirect:/lst-coordinateurs";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("enseignants", enseignantService.getAllEnseignants());
            return "coordinateur-add";
        }
    }

    /**
     * Affiche le formulaire pour modifier un coordinateur
     */
    @GetMapping("/edit-coordinateur/{id}")
    public String editCoordinateur(@PathVariable Long id, Model model) {
        try {
            Coordinateur coordinateur = coordinateurService.getCoordinateurById(id);
            model.addAttribute("coordinateur", coordinateur);
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("enseignants", enseignantService.getAllEnseignants());
            return "coordinateur-edit";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/lst-coordinateurs";
        }
    }

    /**
     * Met à jour un coordinateur existant
     */
    @PostMapping("/update-coordinateur/{id}")
    public String updateCoordinateur(@PathVariable Long id,
                                      @Valid @ModelAttribute("coordinateur") CoordinateurDTO coordinateurDTO,
                                      BindingResult bindingResult,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("enseignants", enseignantService.getAllEnseignants());
            return "coordinateur-edit";
        }

        try {
            coordinateurService.modifierCoordinateur(id, coordinateurDTO);
            return "redirect:/lst-coordinateurs";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("enseignants", enseignantService.getAllEnseignants());
            return "coordinateur-edit";
        }
    }

    /**
     * Affiche les détails d'un coordinateur
     */
    @GetMapping("/view-coordinateur/{id}")
    public String viewCoordinateur(@PathVariable Long id, Model model) {
        try {
            Coordinateur coordinateur = coordinateurService.getCoordinateurById(id);
            model.addAttribute("coordinateur", coordinateur);
            return "coordinateur-detail";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/lst-coordinateurs";
        }
    }

    // ==================== REST API ENDPOINTS ====================

    /**
     * API - Récupère tous les coordinateurs
     */
    @GetMapping("/api/coordinateurs")
    @ResponseBody
    public ResponseEntity<List<Coordinateur>> getAllCoordinateurs() {
        return ResponseEntity.ok(coordinateurService.getAllCoordinateurs());
    }

    /**
     * API - Récupère un coordinateur par son ID
     */
    @GetMapping("/api/coordinateurs/{id}")
    @ResponseBody
    public ResponseEntity<?> getCoordinateurById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(coordinateurService.getCoordinateurById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API - Crée un nouveau coordinateur
     */
    @PostMapping("/api/coordinateurs")
    @ResponseBody
    public ResponseEntity<?> createCoordinateur(@Valid @RequestBody CoordinateurDTO coordinateurDTO) {
        try {
            Coordinateur coordinateur = coordinateurService.creerCoordinateur(coordinateurDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(coordinateur);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * API - Met à jour un coordinateur
     */
    @PutMapping("/api/coordinateurs/{id}")
    @ResponseBody
    public ResponseEntity<?> updateCoordinateur(@PathVariable Long id,
                                                 @Valid @RequestBody CoordinateurDTO coordinateurDTO) {
        try {
            Coordinateur coordinateur = coordinateurService.modifierCoordinateur(id, coordinateurDTO);
            return ResponseEntity.ok(coordinateur);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API - Désactive un coordinateur
     */
    @PatchMapping("/api/coordinateurs/{id}/desactiver")
    @ResponseBody
    public ResponseEntity<String> desactiverCoordinateur(@PathVariable Long id) {
        try {
            coordinateurService.desactiverCoordinateur(id);
            return ResponseEntity.ok("Coordinateur désactivé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API - Réactive un coordinateur
     */
    @PatchMapping("/api/coordinateurs/{id}/reactiver")
    @ResponseBody
    public ResponseEntity<String> reactiverCoordinateur(@PathVariable Long id) {
        try {
            coordinateurService.reactiverCoordinateur(id);
            return ResponseEntity.ok("Coordinateur réactivé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API - Recherche des coordinateurs
     */
    @GetMapping("/api/coordinateurs/search")
    @ResponseBody
    public ResponseEntity<List<Coordinateur>> rechercherCoordinateurs(@RequestParam String term) {
        return ResponseEntity.ok(coordinateurService.rechercherCoordinateurs(term));
    }

    /**
     * API - Récupère les coordinateurs actifs
     */
    @GetMapping("/api/coordinateurs/actifs")
    @ResponseBody
    public ResponseEntity<List<Coordinateur>> getCoordinateursActifs() {
        return ResponseEntity.ok(coordinateurService.getCoordinateursActifs());
    }

    /**
     * API - Supprime un coordinateur
     */
    @DeleteMapping("/api/coordinateurs/{id}")
    @ResponseBody
    public ResponseEntity<String> supprimerCoordinateur(@PathVariable Long id) {
        try {
            coordinateurService.supprimerCoordinateur(id);
            return ResponseEntity.ok("Coordinateur supprimé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
