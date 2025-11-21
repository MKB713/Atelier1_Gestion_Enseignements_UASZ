package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.EC;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.ECService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.UEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ECController {

    @Autowired
    private ECService ecService;

    @Autowired
    private UEService ueService;

    @GetMapping("/ecs")
    public String listECs(
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String q,
            Model model) {

        model.addAttribute("ues", ueService.getAllUEs());

        if ("ajout".equals(mode)) {
            model.addAttribute("ec", new EC());
            model.addAttribute("mode", "ajout");
        }
        else if ("modification".equals(mode) && id != null) {
            try {
                EC ec = ecService.getECById(id)
                        .orElseThrow(() -> new RuntimeException("EC non trouvé"));
                model.addAttribute("ec", ec);
                model.addAttribute("mode", "modification");
            } catch (RuntimeException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        else if ("recherche".equals(mode) && q != null && !q.trim().isEmpty()) {
            model.addAttribute("ecs", ecService.searchByLibelle(q.trim()));
            model.addAttribute("query", q.trim());
            model.addAttribute("mode", "recherche");
        }
        else {
            model.addAttribute("ecs", ecService.getAllECs());
            model.addAttribute("mode", "liste");
        }

        return "ec";
    }

    @PostMapping("/save-ec")
    public String saveEC(@ModelAttribute EC ec, Model model) {
        try {
            ecService.saveEC(ec);
            return "redirect:/ecs?success=EC créé avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("ec", ec);
            model.addAttribute("mode", "ajout");
            model.addAttribute("ues", ueService.getAllUEs());
            model.addAttribute("ecs", ecService.getAllECs());
            return "ec";
        }
    }

    @PostMapping("/update-ec")
    public String updateEC(@ModelAttribute EC ec, Model model) {
        try {
            ecService.saveEC(ec);
            return "redirect:/ecs?success=EC modifié avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("ec", ec);
            model.addAttribute("mode", "modification");
            model.addAttribute("ues", ueService.getAllUEs());
            model.addAttribute("ecs", ecService.getAllECs());
            return "ec";
        }
    }

    @GetMapping("/delete-ec/{id}")
    public String deleteEC(@PathVariable Long id, Model model) {
        try {
            ecService.deleteEC(id);
            return "redirect:/ecs?success=EC supprimé avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("ecs", ecService.getAllECs());
            model.addAttribute("mode", "liste");
            return "ec";
        }
    }
}