package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Ue;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FiliereService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FormationService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.UEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UEController {

    @Autowired
    private UEService ueService;

    @Autowired
    private FormationService formationService;

    @Autowired
    private FiliereService filiereService; // AJOUT pour avoir getAllFiliere()

    @GetMapping("/ues")
    public String listUEs(
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String q,
            Model model) {

        // CORRECTION : Utiliser formationService pour les formations
        model.addAttribute("formations", formationService.getAllFormations());

        if ("ajout".equals(mode)) {
            model.addAttribute("ue", new Ue());
            model.addAttribute("mode", "ajout");
        }
        else if ("modification".equals(mode) && id != null) {
            try {
                Ue ue = ueService.getUEById(id)
                        .orElseThrow(() -> new RuntimeException("UE non trouvée"));
                model.addAttribute("ue", ue);
                model.addAttribute("mode", "modification");
            } catch (RuntimeException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        else if ("recherche".equals(mode) && q != null && !q.trim().isEmpty()) {
            model.addAttribute("ues", ueService.searchByLibelle(q.trim()));
            model.addAttribute("query", q.trim());
            model.addAttribute("mode", "recherche");
        }
        else {
            model.addAttribute("ues", ueService.getAllUEs());
            model.addAttribute("mode", "liste");
        }

        return "ue";
    }

    @PostMapping("/save-ue")
    public String saveUE(@ModelAttribute Ue ue, Model model) {
        try {
            ueService.saveUE(ue);
            return "redirect:/ues?success=UE créée avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("ue", ue);
            model.addAttribute("mode", "ajout");
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("ues", ueService.getAllUEs());
            return "ue";
        }
    }

    @PostMapping("/update-ue")
    public String updateUE(@ModelAttribute Ue ue, Model model) {
        try {
            ueService.saveUE(ue);
            return "redirect:/ues?success=UE modifiée avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("ue", ue);
            model.addAttribute("mode", "modification");
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("ues", ueService.getAllUEs());
            return "ue";
        }
    }

    @GetMapping("/delete-ue/{id}")
    public String deleteUE(@PathVariable Long id, Model model) {
        try {
            ueService.deleteUE(id);
            return "redirect:/ues?success=UE supprimée avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("ues", ueService.getAllUEs());
            model.addAttribute("mode", "liste");
            return "ue";
        }
    }
}