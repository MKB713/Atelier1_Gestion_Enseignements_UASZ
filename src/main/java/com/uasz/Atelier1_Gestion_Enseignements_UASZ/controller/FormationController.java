package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Formation;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FiliereService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FormationService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.NiveauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FormationController {

    @Autowired
    private FormationService formationService;
    @Autowired
    private FiliereService filiereService;
    @Autowired
    private NiveauService niveauService;

    // LISTE
    @GetMapping("/lst-formations")
    public String listFormations(Model model) {
        model.addAttribute("formations", formationService.getAllFormations());
        return "lst-formations";
    }

    // ARCHIVES
    @GetMapping("/lst-formations-archives")
    public String listArchives(Model model) {
        model.addAttribute("formations", formationService.getArchivedFormations());
        return "lst-formations-archives";
    }

    // FORMULAIRES (Ajout / Modif) - Restent identiques
    @GetMapping("/formations/ajouter")
    public String showAddForm(Model model) {
        model.addAttribute("formation", new Formation());
        model.addAttribute("filieres", filiereService.getAllFiliere());
        model.addAttribute("niveaux", niveauService.getAllNiveaux());
        model.addAttribute("titrePage", "Nouvelle Formation");
        return "form-formation";
    }

    @GetMapping("/formations/modifier/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("formation", formationService.getFormationById(id));
            model.addAttribute("filieres", filiereService.getAllFiliere());
            model.addAttribute("niveaux", niveauService.getAllNiveaux());
            model.addAttribute("titrePage", "Modifier la Formation");
            return "form-formation";
        } catch (Exception e) {
            return "redirect:/lst-formations";
        }
    }

    @PostMapping("/save-formation")
    public String saveFormation(@ModelAttribute Formation formation, RedirectAttributes ra) {
        try {
            formationService.save(formation);
            ra.addFlashAttribute("success", "Formation enregistrée !");
            return "redirect:/lst-formations";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            if(formation.getId() != null) return "redirect:/formations/modifier/" + formation.getId();
            return "redirect:/formations/ajouter";
        }
    }

    // ACTIONS AJAX (PATCH) - Identiques au fonctionnement EC
    @PatchMapping("/formations/{id}/archiver")
    public ResponseEntity<?> archiver(@PathVariable Long id) {
        formationService.archiveFormation(id);
        return ResponseEntity.ok("Archivée");
    }

    @PatchMapping("/formations/{id}/activer")
    public ResponseEntity<?> activer(@PathVariable Long id) {
        formationService.activerFormation(id);
        return ResponseEntity.ok("Activée");
    }

    @PatchMapping("/formations/{id}/desactiver")
    public ResponseEntity<?> desactiver(@PathVariable Long id) {
        formationService.desactiverFormation(id);
        return ResponseEntity.ok("Désactivée");
    }

    // Restauration (Formulaire classique)
    @PostMapping("/formations/restaurer/{id}")
    public String restaurer(@PathVariable Long id, RedirectAttributes ra) {
        formationService.activerFormation(id);
        ra.addFlashAttribute("success", "Formation restaurée.");
        return "redirect:/lst-formations-archives";
    }
}