package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Niveau;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.NiveauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NiveauController {

    @Autowired
    private NiveauService niveauService;

    // --- LISTE ---
    @GetMapping("/niveaux")
    public String listNiveaux(Model model) {
        model.addAttribute("niveaux", niveauService.getAllNiveaux());
        return "lst-niveaux"; // Pointe vers le nouveau fichier liste
    }

    // --- FORMULAIRE AJOUT ---
    @GetMapping("/niveaux/ajouter")
    public String showAddForm(Model model) {
        model.addAttribute("niveau", new Niveau());
        model.addAttribute("titrePage", "Nouveau Niveau");
        return "form-niveau"; // Pointe vers le formulaire unique
    }

    // --- FORMULAIRE MODIFICATION ---
    @GetMapping("/niveaux/modifier/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Niveau niveau = niveauService.getNiveauById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Niveau invalide : " + id));
            model.addAttribute("niveau", niveau);
            model.addAttribute("titrePage", "Modifier le Niveau");
            return "form-niveau";
        } catch (Exception e) {
            return "redirect:/niveaux";
        }
    }

    // --- SAUVEGARDE (Create & Update) ---
    @PostMapping("/save-niveau")
    public String saveNiveau(@ModelAttribute Niveau niveau, RedirectAttributes ra) {
        try {
            niveauService.saveNiveau(niveau);
            ra.addFlashAttribute("success", "Niveau enregistré avec succès !");
            return "redirect:/niveaux";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/niveaux/" + (niveau.getId() == null ? "ajouter" : "modifier/" + niveau.getId());
        }
    }

    // --- SUPPRESSION ---
    @GetMapping("/delete-niveau/{id}")
    public String deleteNiveau(@PathVariable Long id, RedirectAttributes ra) {
        try {
            niveauService.deleteNiveau(id);
            ra.addFlashAttribute("success", "Niveau supprimé.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Impossible de supprimer : " + e.getMessage());
        }
        return "redirect:/niveaux";
    }
}