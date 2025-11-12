package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String save(Enseignant enseignant) {
        enseignantService.saveEnseignant(enseignant);
        return "redirect:/lst-enseignants";
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
