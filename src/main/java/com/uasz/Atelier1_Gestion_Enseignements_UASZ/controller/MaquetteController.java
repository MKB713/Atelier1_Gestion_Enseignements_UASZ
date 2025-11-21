package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Maquette;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.MaquetteService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FormationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MaquetteController {

    @Autowired
    private MaquetteService maquetteService;

    @Autowired
    private FormationService formationService;

    @GetMapping("/maquettes")
    public String listMaquettes(
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String q,
            Model model) {

        System.out.println("=== MAQUETTE CONTROLLER CALLED ===");
        System.out.println("Mode: " + mode);
        System.out.println("ID: " + id);
        System.out.println("Query: " + q);

        model.addAttribute("formations", formationService.getAllFormations());

        if ("ajout".equals(mode)) {
            model.addAttribute("maquette", new Maquette());
            model.addAttribute("mode", "ajout");
        }
        else if ("modification".equals(mode) && id != null) {
            try {
                Maquette maquette = maquetteService.getMaquetteById(id)
                        .orElseThrow(() -> new RuntimeException("Maquette non trouvée"));
                model.addAttribute("maquette", maquette);
                model.addAttribute("mode", "modification");
            } catch (RuntimeException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        else if ("recherche".equals(mode) && q != null && !q.trim().isEmpty()) {
            model.addAttribute("maquettes", maquetteService.searchByLibelle(q.trim()));
            model.addAttribute("query", q.trim());
            model.addAttribute("mode", "recherche");
        }
        else {
            model.addAttribute("maquettes", maquetteService.getAllMaquettes());
            model.addAttribute("mode", "liste");
        }

        return "maquette";
    }

    @PostMapping("/save-maquette")
    public String saveMaquette(@ModelAttribute Maquette maquette, Model model) {
        try {
            maquetteService.saveMaquette(maquette);
            return "redirect:/maquettes?success=Maquette créée avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("maquette", maquette);
            model.addAttribute("mode", "ajout");
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("maquettes", maquetteService.getAllMaquettes());
            return "maquette";
        }
    }

    @PostMapping("/update-maquette")
    public String updateMaquette(@ModelAttribute Maquette maquette, Model model) {
        try {
            maquetteService.saveMaquette(maquette);
            return "redirect:/maquettes?success=Maquette modifiée avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("maquette", maquette);
            model.addAttribute("mode", "modification");
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("maquettes", maquetteService.getAllMaquettes());
            return "maquette";
        }
    }

    @GetMapping("/delete-maquette/{id}")
    public String deleteMaquette(@PathVariable Long id, Model model) {
        try {
            maquetteService.deleteMaquette(id);
            return "redirect:/maquettes?success=Maquette supprimée avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("maquettes", maquetteService.getAllMaquettes());
            model.addAttribute("mode", "liste");
            return "maquette";
        }
    }

    @GetMapping("/activate-maquette/{id}")
    public String activateMaquette(@PathVariable Long id) {
        try {
            maquetteService.activateMaquette(id);
            return "redirect:/maquettes?success=Maquette activée avec succès";
        } catch (Exception e) {
            return "redirect:/maquettes?error=" + e.getMessage();
        }
    }
}