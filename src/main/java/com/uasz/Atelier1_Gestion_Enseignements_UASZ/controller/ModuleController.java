package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Module;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.ModuleService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FormationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private FormationService formationService;

    @GetMapping("/modules")
    public String listModules(
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String q,
            Model model) {

        model.addAttribute("formations", formationService.getAllFormations());

        if ("ajout".equals(mode)) {
            model.addAttribute("module", new Module());
            model.addAttribute("mode", "ajout");
        }
        else if ("modification".equals(mode) && id != null) {
            try {
                Module module = moduleService.getModuleById(id)
                        .orElseThrow(() -> new RuntimeException("Module non trouvé"));
                model.addAttribute("module", module);
                model.addAttribute("mode", "modification");
            } catch (RuntimeException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        else if ("recherche".equals(mode) && q != null && !q.trim().isEmpty()) {
            model.addAttribute("modules", moduleService.searchByLibelle(q.trim()));
            model.addAttribute("query", q.trim());
            model.addAttribute("mode", "recherche");
        }
        else {
            model.addAttribute("modules", moduleService.getAllModules());
            model.addAttribute("mode", "liste");
        }

        return "module";
    }

    @PostMapping("/save-module")
    public String saveModule(@ModelAttribute Module module, Model model) {
        try {
            moduleService.saveModule(module);
            return "redirect:/modules?success=Module créé avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("module", module);
            model.addAttribute("mode", "ajout");
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("modules", moduleService.getAllModules());
            return "module";
        }
    }

    @PostMapping("/update-module")
    public String updateModule(@ModelAttribute Module module, Model model) {
        try {
            moduleService.saveModule(module);
            return "redirect:/modules?success=Module modifié avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("module", module);
            model.addAttribute("mode", "modification");
            model.addAttribute("formations", formationService.getAllFormations());
            model.addAttribute("modules", moduleService.getAllModules());
            return "module";
        }
    }

    @GetMapping("/delete-module/{id}")
    public String deleteModule(@PathVariable Long id, Model model) {
        try {
            moduleService.deleteModule(id);
            return "redirect:/modules?success=Module supprimé avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("modules", moduleService.getAllModules());
            model.addAttribute("mode", "liste");
            return "module";
        }
    }
}
// SUPPRIMEZ L'ACCOLADE FERMANTE EN TROP ICI