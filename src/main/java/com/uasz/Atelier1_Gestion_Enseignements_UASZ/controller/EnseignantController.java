package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.EnseignantUpdateDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String save(Enseignant enseignant) {
        enseignantService.saveEnseignant(enseignant);
        return "redirect:/lst-enseignants";
    }

    @GetMapping("/edit-enseignant/{id}")
    public String editEnseignant(@PathVariable Long id, Model model) {
        try {
            Enseignant enseignant = enseignantService.getEnseignantById(id);
            if (enseignant == null) {
                return "redirect:/lst-enseignants?error=Enseignant non trouvé";
            }
            model.addAttribute("enseignant", enseignant);
            return "enseignant-edit";
        } catch (RuntimeException e) {
            return "redirect:/lst-enseignants?error=" + e.getMessage();
        }
    }

    @PostMapping("/update-enseignant")
    public String updateEnseignant(@RequestParam Long id,
                                   @RequestParam(required = false) String grade,
                                   @RequestParam(required = false) String statut,
                                   @RequestParam(required = false) String email,
                                   @RequestParam(required = false) String telephone) {
        EnseignantUpdateDTO updateDTO = new EnseignantUpdateDTO();
        updateDTO.setGrade(grade);
        if (statut != null && !statut.isEmpty()) {
            try {
                updateDTO.setStatut(com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Statut.valueOf(statut));
            } catch (IllegalArgumentException e) {
                // Gérer l'erreur si le statut n'est pas valide
            }
        }
        updateDTO.setEmail(email);
        updateDTO.setTelephone(telephone);

        enseignantService.updateEnseignant(id, updateDTO);
        return "redirect:/lst-enseignants";
    }
}
