package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Module;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.ModuleService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Cycle;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Niveau;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @GetMapping("/lst-modules")
    public String index(Model model) {
        model.addAttribute("modules", moduleService.getAllModules());
        return "module-list";
    }

    @GetMapping("/add-module")
    public String addModule(Model model) {
        Module module = new Module();
        model.addAttribute("module", module);
        model.addAttribute("cycles", Cycle.values());
        model.addAttribute("niveaux", Niveau.values());
        return "module-add";
    }

    @PostMapping("/save-module")
    public String save(@ModelAttribute("module") Module module) {
        moduleService.addModule(module);
        return "redirect:/lst-modules";
    }

    @GetMapping("/edit-module/{id}")
    public String editModule(@PathVariable Long id, Model model) {
        Module module = moduleService.getModuleById(id);
        model.addAttribute("module", module);
        model.addAttribute("cycles", Cycle.values());
        model.addAttribute("niveaux", Niveau.values());
        return "module-add";
    }

    @GetMapping("/delete-module/{id}")
    public String deleteModule(@PathVariable Long id) {
        moduleService.deleteModule(id);
        return "redirect:/lst-modules";
    }

    // ========== API REST pour les modales ==========

    @GetMapping("/api/module/{id}")
    @ResponseBody
    public Module getModuleJSON(@PathVariable Long id) {
        return moduleService.getModuleById(id);
    }
}
