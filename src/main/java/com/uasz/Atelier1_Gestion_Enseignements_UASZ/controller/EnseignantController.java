package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.exceptions.MatriculeAlreadyExistsException;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EnseignantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
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



    @PatchMapping("/{id}/archiver")
    @ResponseBody
    public ResponseEntity<String> archiverEnseignant(@PathVariable Long id) {
        enseignantService.archiverEnseignant(id);
        return ResponseEntity.ok("L'enseignant avec l'id " + id + " a été archivé avec succès.");
    }

    @GetMapping("/lst-archives")
    public String listeArchives(Model model) {
        model.addAttribute("enseignants", enseignantService.getAllEnseignantsArchives());
        return "enseignant-archive-list";
    }

}
