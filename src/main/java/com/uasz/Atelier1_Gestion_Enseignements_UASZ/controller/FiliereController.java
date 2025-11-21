package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Filiere;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FiliereController {

    @Autowired
    private FiliereService filiereService;

    // --- LISTE ---
    @GetMapping("/lst-filieres")
    public String listFilieres(Model model) {
        model.addAttribute("filieres", filiereService.getAllFiliere());
        return "lst-filieres"; // Pointe vers la nouvelle liste
    }

    // --- FORMULAIRE AJOUT ---
    @GetMapping("/filieres/ajouter")
    public String showAddForm(Model model) {
        model.addAttribute("filiere", new Filiere());
        model.addAttribute("titrePage", "Nouvelle Filière");
        return "form-filiere"; // Pointe vers le formulaire unique
    }

    // --- FORMULAIRE MODIFICATION ---
    @GetMapping("/filieres/modifier/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Filiere filiere = filiereService.getFiliereById(id);
            model.addAttribute("filiere", filiere);
            model.addAttribute("titrePage", "Modifier la Filière");
            return "form-filiere";
        } catch (Exception e) {
            return "redirect:/lst-filieres";
        }
    }

    // --- SAUVEGARDE (Create & Update) ---
    @PostMapping("/save-filiere")
    public String saveFiliere(@ModelAttribute Filiere filiere, RedirectAttributes ra) {
        try {
            filiereService.save(filiere);
            ra.addFlashAttribute("success", "Filière enregistrée avec succès !");
            return "redirect:/lst-filieres";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            // Redirection intelligente en cas d'erreur
            return "redirect:/filieres/" + (filiere.getId() == null ? "ajouter" : "modifier/" + filiere.getId());
        }
    }

    // --- SUPPRESSION ---
    @GetMapping("/delete-filiere/{id}")
    public String deleteFiliere(@PathVariable Long id, RedirectAttributes ra) {
        try {
            filiereService.delete(id);
            ra.addFlashAttribute("success", "Filière supprimée.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Impossible de supprimer : " + e.getMessage());
        }
        return "redirect:/lst-filieres";
    }
}