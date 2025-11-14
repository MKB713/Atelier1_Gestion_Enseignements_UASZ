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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/recherche")
    public String rechercherEnseignant(@RequestParam(required = false) Long matricule,
                                       Model model) {
        List<Enseignant> enseignants = new ArrayList<>();

        // Si pas de matricule, retourner la vue vide
        if (matricule == null) {
            model.addAttribute("enseignants", enseignants);
            return "redirect:/lst-enseignants";
        }
        // Rechercher l'enseignant
        Optional<Enseignant> enseignantOpt = enseignantService.rechercherEnseignant(matricule);

        if (enseignantOpt.isPresent()) {
            // Enseignant trouvé
            enseignants.add(enseignantOpt.get());
            model.addAttribute("message", "Enseignant trouvé ✓");
        } else {
            // Enseignant non trouvé
            model.addAttribute("message", "Aucun enseignant avec le matricule : " + matricule);
        }

        model.addAttribute("enseignants", enseignants);
        model.addAttribute("matricule", matricule);

        return "resultatRecherche";
    }

}
