package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.EnseignantUpdateDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Statut;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EnseignantController {

    @Autowired
    private EnseignantService enseignantService;

    // ----------------------------------------------------------------------
    // --- LISTES & VUES PRINCIPALES ---
    // ----------------------------------------------------------------------
    @GetMapping("/lst-enseignants")
    public String listEnseignants(Model model) {
        List<Enseignant> enseignants = enseignantService.getAllEnseignants();
        model.addAttribute("enseignants", enseignants);
        return "enseignant-list";
    }

    @GetMapping("/lst-enseignants-archives")
    public String listEnseignantsArchives(Model model) {
        model.addAttribute("enseignants", enseignantService.getAllEnseignantsArchives());
        model.addAttribute("isArchiveView", true);
        return "enseignant-archive-list";
    }

    @GetMapping("/view-enseignant/{id}")
    public String viewEnseignantDetails(@PathVariable Long id, Model model) {
        try {
            Enseignant enseignant = enseignantService.getEnseignantById(id);
            model.addAttribute("enseignant", enseignant);
        } catch (RuntimeException e) {
            model.addAttribute("enseignant", null);
            model.addAttribute("error", e.getMessage());
        }
        return "enseignant-details";
    }

    // ----------------------------------------------------------------------
    // --- CREATION (GET & POST) ---
    // ----------------------------------------------------------------------
    @GetMapping("/add-enseignant")
    public String addEnseignant(Model model) {
        model.addAttribute("enseignant", new Enseignant());
        model.addAttribute("grades", List.of("Assistant", "Maître-Assistant", "Maître de Conférences", "Professeur Titulaire", "Professeur Assimilé"));
        model.addAttribute("statuts", Statut.values());
        return "enseignant-add";
    }

    @PostMapping("/save-enseignant")
    public String saveEnseignant(@ModelAttribute("enseignant") Enseignant enseignant,
                                 RedirectAttributes redirectAttributes) {
        try {
            enseignantService.saveEnseignant(enseignant);
            redirectAttributes.addFlashAttribute("success", "Enseignant ajouté avec succès! Matricule: " + enseignant.getMatricule());
            return "redirect:/lst-enseignants";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/add-enseignant";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/add-enseignant";
        }
    }

    // ----------------------------------------------------------------------
    // --- MODIFICATION (GET & POST) ---
    // ----------------------------------------------------------------------

    @GetMapping("/edit-enseignant/{id}")
    public String editEnseignant(@PathVariable Long id, Model model) {
        Enseignant enseignant = enseignantService.getEnseignantById(id);

        model.addAttribute("enseignant", enseignant);
        model.addAttribute("grades", List.of("Assistant", "Maître-Assistant", "Maître de Conférences", "Professeur Titulaire", "Professeur Assimilé"));
        model.addAttribute("statuts", Statut.values()); // Assurez-vous d'envoyer l'enum Statut

        return "enseignant-edit";
    }

    /**
     * Traite la soumission du formulaire d'édition. Utilise l'entité Enseignant pour la liaison.
     */
    @PostMapping("/update-enseignant/{id}")
    public String updateEnseignant(@PathVariable Long id,
                                   @ModelAttribute Enseignant enseignantForm, // Utilisation de l'entité
                                   RedirectAttributes redirectAttributes) {
        try {
            Enseignant updatedEnseignant = enseignantService.updateEnseignant(id, enseignantForm);
            redirectAttributes.addFlashAttribute("success", "Enseignant " + updatedEnseignant.getMatricule() + " mis à jour avec succès.");
            return "redirect:/lst-enseignants";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/edit-enseignant/" + id;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification: " + e.getMessage());
            return "redirect:/edit-enseignant/" + id;
        }
    }

    // ----------------------------------------------------------------------
    // --- GESTION DU STATUT (POST) ---
    // ----------------------------------------------------------------------

    @PostMapping("/archive-enseignant/{id}")
    public String archiverEnseignant(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            enseignantService.archiverEnseignant(id);
            redirectAttributes.addFlashAttribute("success", "Enseignant archivé avec succès.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'archivage: " + e.getMessage());
        }
        return "redirect:/lst-enseignants";
    }

    @PostMapping("/unarchive-enseignant/{id}") // <--- C'EST CETTE URL QUI COMPTE
    public String desarchiverEnseignant(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            enseignantService.desarchiverEnseignant(id);
            redirectAttributes.addFlashAttribute("success", "Enseignant désarchivé et activé avec succès.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors du désarchivage: " + e.getMessage());
        }
        return "redirect:/lst-enseignants-archives";
    }

    @PostMapping("/activer-enseignant/{id}")
    public String activerEnseignant(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            enseignantService.activerEnseignant(id);
            redirectAttributes.addFlashAttribute("success", "Enseignant activé avec succès.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'activation: " + e.getMessage());
        }
        return "redirect:/lst-enseignants";
    }

    @PostMapping("/desactiver-enseignant/{id}")
    public String desactiverEnseignant(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            enseignantService.desactiverEnseignant(id);
            redirectAttributes.addFlashAttribute("success", "Enseignant désactivé avec succès.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la désactivation: " + e.getMessage());
        }
        return "redirect:/lst-enseignants";
    }
}