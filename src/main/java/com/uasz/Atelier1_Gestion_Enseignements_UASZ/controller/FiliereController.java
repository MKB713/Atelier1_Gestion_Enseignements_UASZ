package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Filiere;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FiliereController {

    @Autowired
    private FiliereService filiereService;

    @GetMapping("/lst-filieres")
    public String listFilieres(
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) Long id,
            Model model) {

        if ("ajout".equals(mode)) {
            model.addAttribute("filiere", new Filiere());
            model.addAttribute("mode", "ajout");
        }
        else if ("modification".equals(mode) && id != null) {
            Filiere filiere = filiereService.getFiliereById(id);
            model.addAttribute("filiere", filiere);
            model.addAttribute("mode", "modification");
        }
        else {
            model.addAttribute("filieres", filiereService.getAllFiliere());
            model.addAttribute("mode", "liste");
        }

        return "filiere";
    }

    @PostMapping("/save-filiere")
    public String saveFiliere(@ModelAttribute Filiere filiere, Model model) {
        try {
            filiereService.save(filiere);
            return "redirect:/lst-filieres";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("filiere", filiere);
            model.addAttribute("mode", "ajout");
            model.addAttribute("filieres", filiereService.getAllFiliere());
            return "filiere";
        }
    }

    @PostMapping("/update-filiere")
    public String updateFiliere(@ModelAttribute Filiere filiere, Model model) {
        try {
            filiereService.save(filiere);
            return "redirect:/lst-filieres";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("filiere", filiere);
            model.addAttribute("mode", "modification");
            model.addAttribute("filieres", filiereService.getAllFiliere());
            return "filiere";
        }
    }

    @GetMapping("/delete-filiere/{id}")
    public String deleteFiliere(@PathVariable Long id, Model model) {
        try {
            filiereService.delete(id);
            return "redirect:/lst-filieres";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("filieres", filiereService.getAllFiliere());
            model.addAttribute("mode", "liste");
            return "filiere";
        }
    }
}