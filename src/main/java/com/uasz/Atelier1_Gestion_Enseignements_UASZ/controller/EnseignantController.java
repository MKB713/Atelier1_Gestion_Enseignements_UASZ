package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    // ➕ Ajouter à la fin de la classe EnseignantController

    @GetMapping("/enseignants/toggle/{id}")
    public String toggleActif(@PathVariable Long id) {
        enseignantService.changerStatutActif(id);
        return "redirect:/lst-enseignants";
    }

    @PatchMapping("/enseignants/{id}/activer")
    @ResponseBody
    public String activerEnseignant(@PathVariable Long id) {
        enseignantService.activerEnseignant(id);
        return "L'enseignant a été activé avec succès.";
    }

    @PatchMapping("/enseignants/{id}/desactiver")
    @ResponseBody
    public String desactiverEnseignant(@PathVariable Long id) {
        enseignantService.desactiverEnseignant(id);
        return "L'enseignant a été désactivé avec succès.";
    }
}
