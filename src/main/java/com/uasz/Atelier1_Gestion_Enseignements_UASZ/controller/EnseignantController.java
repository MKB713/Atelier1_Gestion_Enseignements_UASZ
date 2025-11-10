package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/enseignants")
public class EnseignantController {

    @Autowired
    private EnseignantService enseignantService;

    //Recherche un enseignant par matricule

    @GetMapping("/recherche")
    public String rechercherEnseignant(@RequestParam(required = false) Long matricule,
                                       Model model) {
        List<Enseignant> enseignants = new ArrayList<>();

        // Si pas de matricule, retourner la vue vide
        if (matricule == null) {
            model.addAttribute("enseignants", enseignants);
            return "listeEnseignants";
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