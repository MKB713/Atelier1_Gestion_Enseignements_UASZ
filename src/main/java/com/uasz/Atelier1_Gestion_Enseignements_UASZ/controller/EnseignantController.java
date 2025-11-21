package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.exceptions.MatriculeAlreadyExistsException;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EnseignantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class EnseignantController {

    @Autowired
    private EnseignantService enseignantService;

    @RequestMapping("/lst-enseignants")
    public String index(Model model) {
        model.addAttribute("enseignants", enseignantService.getAllEnseignants());
        return "enseignant-list";
    }

    @RequestMapping("/add-enseignant")
    public String addEnseignant(Model model) {
        Enseignant enseignant = new Enseignant();
        model.addAttribute("enseignant", enseignant);
        return "enseignant-add";
    }

    @RequestMapping("/save-enseignant")
    public String save(@Valid Enseignant enseignant, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Erreur de validation: " + error.getDefaultMessage());
            });
            return "enseignant-add";
        }

        try {
            enseignantService.saveEnseignant(enseignant);
            return "redirect:/lst-enseignants";
        } catch (MatriculeAlreadyExistsException e) {
            model.addAttribute("matriculeError", e.getMessage());
            return "enseignant-add";
        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de la sauvegarde: " + e.getMessage());
            return "enseignant-add";
        }
    }

    // ✅ Archiver un enseignant et rediriger vers la liste des actifs
    @PostMapping("/archiver/{id}")
    public String archiverEnseignant(@PathVariable Long id) {
        enseignantService.archiverEnseignant(id);
        return "redirect:/lst-enseignants";
    }

    @GetMapping("/lst-archives")
    public String listeArchives(Model model) {
        model.addAttribute("enseignants", enseignantService.getAllEnseignantsArchives());
        return "enseignant-archive-list";
    }

    // ✅ Désarchiver un enseignant et rediriger vers la liste des archivés
    @PostMapping("/desarchiver/{id}")
    public String desarchiverEnseignant(@PathVariable Long id) {
        enseignantService.desarchiverEnseignant(id);
        return "redirect:/lst-archives";
    }

    @GetMapping("/view-enseignant/{id}")
    public String viewEnseignant(@PathVariable Long id, Model model) {
        Enseignant enseignant = enseignantService.getEnseignantById(id);

        if (enseignant != null) {
            model.addAttribute("enseignant", enseignant);
            return "enseignant-details"; // vue normale
        } else {
            model.addAttribute("errorMessage", "Enseignant introuvable avec l'ID " + id);
            return "error-page"; // vue d'erreur personnalisée
        }
    }

    @GetMapping("/edit-enseignant/{id}")
    public String editEnseignant(@PathVariable Long id, Model model) {
        Enseignant enseignant = enseignantService.getEnseignantById(id);
        model.addAttribute("enseignant", enseignant);
        return "enseignant-add";
    }
}