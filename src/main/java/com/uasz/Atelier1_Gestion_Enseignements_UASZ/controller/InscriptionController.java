package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.InscriptionDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Inscription;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.ClasseService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EtudiantService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FormationService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.InscriptionService;
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
public class InscriptionController {

    @Autowired
    private InscriptionService inscriptionService;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private ClasseService classeService;

    @Autowired
    private FormationService formationService;

    /**
     * Affiche la liste de toutes les inscriptions
     */
    @GetMapping("/lst-inscriptions")
    public String listInscriptions(Model model,
                                    @RequestParam(required = false) String annee) {
        List<Inscription> inscriptions;
        if (annee != null && !annee.isEmpty()) {
            inscriptions = inscriptionService.getInscriptionsParAnnee(annee);
        } else {
            inscriptions = inscriptionService.getAllInscriptions();
        }
        model.addAttribute("inscriptions", inscriptions);
        model.addAttribute("anneeSelectionnee", annee);
        return "inscription-list";
    }

    /**
     * Affiche le formulaire pour ajouter une inscription
     */
    @GetMapping("/add-inscription")
    public String addInscription(Model model,
                                  @RequestParam(required = false) Long etudiantId) {
        InscriptionDTO inscriptionDTO = new InscriptionDTO();
        if (etudiantId != null) {
            inscriptionDTO.setEtudiantId(etudiantId);
        }
        model.addAttribute("inscription", inscriptionDTO);
        model.addAttribute("etudiants", etudiantService.getEtudiantsActifs());
        model.addAttribute("classes", classeService.getAllClasses());
        model.addAttribute("formations", formationService.getAllFormations());
        return "inscription-add";
    }

    /**
     * Enregistre une nouvelle inscription
     */
    @PostMapping("/save-inscription")
    public String saveInscription(@Valid @ModelAttribute("inscription") InscriptionDTO inscriptionDTO,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("etudiants", etudiantService.getEtudiantsActifs());
            model.addAttribute("classes", classeService.getAllClasses());
            model.addAttribute("formations", formationService.getAllFormations());
            return "inscription-add";
        }

        try {
            inscriptionService.creerInscription(inscriptionDTO);
            return "redirect:/lst-inscriptions";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("etudiants", etudiantService.getEtudiantsActifs());
            model.addAttribute("classes", classeService.getAllClasses());
            model.addAttribute("formations", formationService.getAllFormations());
            return "inscription-add";
        }
    }

    /**
     * Affiche le formulaire pour modifier une inscription
     */
    @GetMapping("/edit-inscription/{id}")
    public String editInscription(@PathVariable Long id, Model model) {
        try {
            Inscription inscription = inscriptionService.getInscriptionById(id);
            model.addAttribute("inscription", inscription);
            model.addAttribute("etudiants", etudiantService.getEtudiantsActifs());
            model.addAttribute("classes", classeService.getAllClasses());
            model.addAttribute("formations", formationService.getAllFormations());
            return "inscription-edit";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/lst-inscriptions";
        }
    }

    /**
     * Met à jour une inscription existante
     */
    @PostMapping("/update-inscription/{id}")
    public String updateInscription(@PathVariable Long id,
                                     @Valid @ModelAttribute("inscription") InscriptionDTO inscriptionDTO,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("etudiants", etudiantService.getEtudiantsActifs());
            model.addAttribute("classes", classeService.getAllClasses());
            model.addAttribute("formations", formationService.getAllFormations());
            return "inscription-edit";
        }

        try {
            inscriptionService.modifierInscription(id, inscriptionDTO);
            return "redirect:/lst-inscriptions";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("etudiants", etudiantService.getEtudiantsActifs());
            model.addAttribute("classes", classeService.getAllClasses());
            model.addAttribute("formations", formationService.getAllFormations());
            return "inscription-edit";
        }
    }

    /**
     * Affiche les détails d'une inscription
     */
    @GetMapping("/view-inscription/{id}")
    public String viewInscription(@PathVariable Long id, Model model) {
        try {
            Inscription inscription = inscriptionService.getInscriptionById(id);
            model.addAttribute("inscription", inscription);
            return "inscription-detail";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/lst-inscriptions";
        }
    }

    /**
     * Affiche les inscriptions d'un étudiant
     */
    @GetMapping("/inscriptions-etudiant/{etudiantId}")
    public String viewInscriptionsEtudiant(@PathVariable Long etudiantId, Model model) {
        try {
            model.addAttribute("etudiant", etudiantService.getEtudiantById(etudiantId));
            model.addAttribute("inscriptions", inscriptionService.getInscriptionsEtudiant(etudiantId));
            return "etudiant-inscriptions";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/lst-etudiants";
        }
    }

    // ==================== REST API ENDPOINTS ====================

    /**
     * API - Récupère toutes les inscriptions
     */
    @GetMapping("/api/inscriptions")
    @ResponseBody
    public ResponseEntity<List<Inscription>> getAllInscriptions() {
        return ResponseEntity.ok(inscriptionService.getAllInscriptions());
    }

    /**
     * API - Récupère une inscription par son ID
     */
    @GetMapping("/api/inscriptions/{id}")
    @ResponseBody
    public ResponseEntity<?> getInscriptionById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(inscriptionService.getInscriptionById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API - Crée une nouvelle inscription
     */
    @PostMapping("/api/inscriptions")
    @ResponseBody
    public ResponseEntity<?> createInscription(@Valid @RequestBody InscriptionDTO inscriptionDTO) {
        try {
            Inscription inscription = inscriptionService.creerInscription(inscriptionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(inscription);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * API - Met à jour une inscription
     */
    @PutMapping("/api/inscriptions/{id}")
    @ResponseBody
    public ResponseEntity<?> updateInscription(@PathVariable Long id,
                                                @Valid @RequestBody InscriptionDTO inscriptionDTO) {
        try {
            Inscription inscription = inscriptionService.modifierInscription(id, inscriptionDTO);
            return ResponseEntity.ok(inscription);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API - Valide une inscription
     */
    @PatchMapping("/api/inscriptions/{id}/valider")
    @ResponseBody
    public ResponseEntity<String> validerInscription(@PathVariable Long id) {
        try {
            inscriptionService.validerInscription(id);
            return ResponseEntity.ok("Inscription validée avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API - Annule une inscription
     */
    @PatchMapping("/api/inscriptions/{id}/annuler")
    @ResponseBody
    public ResponseEntity<String> annulerInscription(@PathVariable Long id) {
        try {
            inscriptionService.annulerInscription(id);
            return ResponseEntity.ok("Inscription annulée avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API - Récupère les inscriptions d'un étudiant
     */
    @GetMapping("/api/inscriptions/etudiant/{etudiantId}")
    @ResponseBody
    public ResponseEntity<List<Inscription>> getInscriptionsEtudiant(@PathVariable Long etudiantId) {
        return ResponseEntity.ok(inscriptionService.getInscriptionsEtudiant(etudiantId));
    }

    /**
     * API - Récupère les inscriptions d'une classe
     */
    @GetMapping("/api/inscriptions/classe/{classeId}")
    @ResponseBody
    public ResponseEntity<List<Inscription>> getInscriptionsClasse(@PathVariable Long classeId) {
        return ResponseEntity.ok(inscriptionService.getInscriptionsClasse(classeId));
    }

    /**
     * API - Supprime une inscription
     */
    @DeleteMapping("/api/inscriptions/{id}")
    @ResponseBody
    public ResponseEntity<String> supprimerInscription(@PathVariable Long id) {
        try {
            inscriptionService.supprimerInscription(id);
            return ResponseEntity.ok("Inscription supprimée avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
