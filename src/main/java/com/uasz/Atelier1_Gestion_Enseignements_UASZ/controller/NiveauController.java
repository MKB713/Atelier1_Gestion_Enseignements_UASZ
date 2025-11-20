package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Niveau;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.NiveauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NiveauController {

    @Autowired
    private NiveauService niveauService;

    @GetMapping("/niveaux")
    public String listNiveaux(
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) Long id,
            Model model) {

        // Toujours ajouter la liste des niveaux pour éviter les erreurs
        model.addAttribute("niveaux", niveauService.getAllNiveaux());

        if ("ajout".equals(mode)) {
            model.addAttribute("niveau", new Niveau());
            model.addAttribute("mode", "ajout");
        }
        else if ("modification".equals(mode) && id != null) {
            try {
                Niveau niveau = niveauService.getNiveauById(id)
                        .orElseThrow(() -> new RuntimeException("Niveau non trouvé"));
                model.addAttribute("niveau", niveau);
                model.addAttribute("mode", "modification");
            } catch (RuntimeException e) {
                model.addAttribute("error", e.getMessage());
                model.addAttribute("mode", "liste");
            }
        }
        else {
            model.addAttribute("mode", "liste");
        }

        return "niveau";
    }

    @PostMapping("/save-niveau")
    public String saveNiveau(@ModelAttribute Niveau niveau, Model model) {
        try {
            niveauService.saveNiveau(niveau);
            return "redirect:/niveaux";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("niveau", niveau);
            model.addAttribute("mode", "ajout");
            model.addAttribute("niveaux", niveauService.getAllNiveaux());
            return "niveau";
        }
    }

    @PostMapping("/update-niveau")
    public String updateNiveau(@ModelAttribute Niveau niveau, Model model) {
        try {
            niveauService.saveNiveau(niveau);
            return "redirect:/niveaux";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("niveau", niveau);
            model.addAttribute("mode", "modification");
            model.addAttribute("niveaux", niveauService.getAllNiveaux());
            return "niveau";
        }
    }

    @GetMapping("/delete-niveau/{id}")
    public String deleteNiveau(@PathVariable Long id, Model model) {
        try {
            niveauService.deleteNiveau(id);
            return "redirect:/niveaux";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("niveaux", niveauService.getAllNiveaux());
            model.addAttribute("mode", "liste");
            return "niveau";
        }
    }
}