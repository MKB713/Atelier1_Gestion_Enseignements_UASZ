package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Formation;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutFormation;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FiliereService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FormationService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.NiveauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FormationController {

    @Autowired
    private FormationService formationService;

    @Autowired
    private FiliereService filiereService;

    @Autowired
    private NiveauService niveauService;

    /* ************************************
     * PAGE UNIQUE FORMATION - TOUS LES MODES
     *************************************/
    @GetMapping("/lst-formations")
    public String gestionFormations(
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String archive,
            Model model) {

        // Toujours ajouter les listes de base pour les formulaires
        model.addAttribute("filieres", filiereService.getAllFiliere());
        model.addAttribute("niveaux", niveauService.getAllNiveaux());

        if ("ajout".equals(mode)) {
            // MODE AJOUT
            model.addAttribute("formation", new Formation());
            model.addAttribute("mode", "ajout");
        }
        else if ("modification".equals(mode) && id != null) {
            // MODE MODIFICATION
            Formation formation = formationService.getFormationById(id)
                    .orElseThrow(() -> new RuntimeException("Formation non trouvée"));
            model.addAttribute("formation", formation);
            model.addAttribute("mode", "modification");
        }
        else if ("recherche".equals(mode) && q != null && !q.trim().isEmpty()) {
            // MODE RECHERCHE
            model.addAttribute("formations", formationService.searchByLibelle(q.trim()));
            model.addAttribute("query", q.trim());
            model.addAttribute("mode", "recherche");
        }
        else if ("true".equals(archive)) {
            // MODE ARCHIVES
            model.addAttribute("formations", formationService.getAllFormations().stream()
                    .filter(f -> f.getStatutFormation() == StatutFormation.ARCHIVE)
                    .toList());
            model.addAttribute("mode", "archive");
        }
        else {
            // MODE LISTE (par défaut) - formations actives
            model.addAttribute("formations", formationService.getActiveFormations());
            model.addAttribute("mode", "liste");
        }

        return "formation";
    }

    /* ************************************
     * SAUVEGARDE NOUVELLE FORMATION
     *************************************/
    @PostMapping("/save-formation")
    public String saveFormation(@ModelAttribute Formation formation, Model model) {
        try {
            formationService.createFormation(formation);
            return "redirect:/lst-formations";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("formation", formation);
            model.addAttribute("mode", "ajout");
            model.addAttribute("filieres", filiereService.getAllFiliere());
            model.addAttribute("niveaux", niveauService.getAllNiveaux());
            return "formation";
        }
    }

    /* ************************************
     * UPDATE FORMATION
     *************************************/
    @PostMapping("/update-formation")
    public String updateFormation(@ModelAttribute Formation formation, Model model) {
        try {
            formationService.updateFormation(formation.getId(), formation);
            return "redirect:/lst-formations";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("formation", formation);
            model.addAttribute("mode", "modification");
            model.addAttribute("filieres", filiereService.getAllFiliere());
            model.addAttribute("niveaux", niveauService.getAllNiveaux());
            return "formation";
        }
    }

    /* ************************************
     * ARCHIVAGE (PATCH)
     *************************************/
    @PatchMapping("/formations/{id}/archiver")
    @ResponseBody
    public ResponseEntity<String> archiverFormation(@PathVariable Long id) {
        try {
            formationService.archiveFormation(id);
            return ResponseEntity.ok("Formation archivée avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /* ************************************
     * ACTIVATION (PATCH)
     *************************************/
    @PatchMapping("/formations/{id}/activer")
    @ResponseBody
    public ResponseEntity<String> activerFormation(@PathVariable Long id) {
        try {
            formationService.activerFormation(id);
            return ResponseEntity.ok("Formation activée avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}