package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.ResponsableDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Responsable;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EnseignantService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FormationService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.ResponsableService;
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
public class ResponsableController {

    @Autowired
    private ResponsableService responsableService;

    @Autowired
    private FormationService formationService;

    @Autowired
    private EnseignantService enseignantService;

    /**
     * Affiche la liste de tous les responsables
     */
    @GetMapping("/lst-responsables")
    public String listResponsables(Model model) {
        model.addAttribute("responsables", responsableService.getAllResponsables());
        model.addAttribute("responsablesLicence", responsableService.getResponsablesLicence());
        model.addAttribute("responsablesMaster", responsableService.getResponsablesMaster());
        return "responsable-list";
    }

    /**
     * Affiche le formulaire pour ajouter un responsable
     */
    @GetMapping("/add-responsable")
    public String addResponsable(Model model) {
        ResponsableDTO responsableDTO = new ResponsableDTO();
        model.addAttribute("responsable", responsableDTO);
        model.addAttribute("formations", formationService.getAllFormations());
        model.addAttribute("enseignants", enseignantService.getAllEnseignants());
        return "responsable-add";
    }

    /**
     * Enregistre un nouveau responsable
     */
    @PostMapping("/save-responsable")
    public String saveResponsable(@Valid @ModelAttribute("responsable") ResponsableDTO responsableDTO,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("enseignants", enseignantService.getAllEnseignants());
            return "responsable-add";
        }

        try {
            responsableService.creerResponsable(responsableDTO);
            return "redirect:/lst-responsables";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("enseignants", enseignantService.getAllEnseignants());
            return "responsable-add";
        }
    }

    /**
     * Affiche le formulaire pour modifier un responsable
     */
    @GetMapping("/edit-responsable/{id}")
    public String editResponsable(@PathVariable Long id, Model model) {
        try {
            Responsable responsable = responsableService.getResponsableById(id);
            model.addAttribute("responsable", responsable);
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("enseignants", enseignantService.getAllEnseignants());
            return "responsable-edit";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/lst-responsables";
        }
    }

    /**
     * Met à jour un responsable existant
     */
    @PostMapping("/update-responsable/{id}")
    public String updateResponsable(@PathVariable Long id,
                                     @Valid @ModelAttribute("responsable") ResponsableDTO responsableDTO,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("enseignants", enseignantService.getAllEnseignants());
            return "responsable-edit";
        }

        try {
            responsableService.modifierResponsable(id, responsableDTO);
            return "redirect:/lst-responsables";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("enseignants", enseignantService.getAllEnseignants());
            return "responsable-edit";
        }
    }

    /**
     * Affiche les détails d'un responsable
     */
    @GetMapping("/view-responsable/{id}")
    public String viewResponsable(@PathVariable Long id, Model model) {
        try {
            Responsable responsable = responsableService.getResponsableById(id);
            model.addAttribute("responsable", responsable);
            return "responsable-detail";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/lst-responsables";
        }
    }

    // ==================== REST API ENDPOINTS ====================

    /**
     * API - Récupère tous les responsables
     */
    @GetMapping("/api/responsables")
    @ResponseBody
    public ResponseEntity<List<Responsable>> getAllResponsables() {
        return ResponseEntity.ok(responsableService.getAllResponsables());
    }

    /**
     * API - Récupère un responsable par son ID
     */
    @GetMapping("/api/responsables/{id}")
    @ResponseBody
    public ResponseEntity<?> getResponsableById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(responsableService.getResponsableById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API - Crée un nouveau responsable
     */
    @PostMapping("/api/responsables")
    @ResponseBody
    public ResponseEntity<?> createResponsable(@Valid @RequestBody ResponsableDTO responsableDTO) {
        try {
            Responsable responsable = responsableService.creerResponsable(responsableDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responsable);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * API - Met à jour un responsable
     */
    @PutMapping("/api/responsables/{id}")
    @ResponseBody
    public ResponseEntity<?> updateResponsable(@PathVariable Long id,
                                                @Valid @RequestBody ResponsableDTO responsableDTO) {
        try {
            Responsable responsable = responsableService.modifierResponsable(id, responsableDTO);
            return ResponseEntity.ok(responsable);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API - Désactive un responsable
     */
    @PatchMapping("/api/responsables/{id}/desactiver")
    @ResponseBody
    public ResponseEntity<String> desactiverResponsable(@PathVariable Long id) {
        try {
            responsableService.desactiverResponsable(id);
            return ResponseEntity.ok("Responsable désactivé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API - Réactive un responsable
     */
    @PatchMapping("/api/responsables/{id}/reactiver")
    @ResponseBody
    public ResponseEntity<String> reactiverResponsable(@PathVariable Long id) {
        try {
            responsableService.reactiverResponsable(id);
            return ResponseEntity.ok("Responsable réactivé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API - Recherche des responsables
     */
    @GetMapping("/api/responsables/search")
    @ResponseBody
    public ResponseEntity<List<Responsable>> rechercherResponsables(@RequestParam String term) {
        return ResponseEntity.ok(responsableService.rechercherResponsables(term));
    }

    /**
     * API - Récupère les responsables actifs
     */
    @GetMapping("/api/responsables/actifs")
    @ResponseBody
    public ResponseEntity<List<Responsable>> getResponsablesActifs() {
        return ResponseEntity.ok(responsableService.getResponsablesActifs());
    }

    /**
     * API - Supprime un responsable
     */
    @DeleteMapping("/api/responsables/{id}")
    @ResponseBody
    public ResponseEntity<String> supprimerResponsable(@PathVariable Long id) {
        try {
            responsableService.supprimerResponsable(id);
            return ResponseEntity.ok("Responsable supprimé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
