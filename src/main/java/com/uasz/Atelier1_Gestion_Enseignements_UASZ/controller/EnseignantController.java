package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
