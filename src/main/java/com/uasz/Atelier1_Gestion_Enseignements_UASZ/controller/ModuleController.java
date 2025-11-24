package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Module;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Cycle;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Niveau;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    // --- VUE UNIQUE (LISTE + MODALE) ---
    @GetMapping("/lst-modules")
    public String index(Model model) {
        // Liste des modules actifs
        model.addAttribute("modules", moduleService.getAllModules());

        // Listes pour les menus déroulants du formulaire
        model.addAttribute("cycles", Cycle.values());
        model.addAttribute("niveaux", Niveau.values());

        // Objet vide pour le formulaire d'ajout
        model.addAttribute("module", new Module());

        return "lst-modules";
    }

    // --- ENREGISTREMENT (AJOUT & MODIF) ---
    @PostMapping("/save-module")
    public String save(@ModelAttribute Module module, RedirectAttributes ra) {
        try {
            if (module.getId() != null) {
                moduleService.updateModule(module.getId(), module);
                ra.addFlashAttribute("success", "Module mis à jour avec succès.");
            } else {
                moduleService.addModule(module);
                ra.addFlashAttribute("success", "Nouveau module ajouté.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
        }
        return "redirect:/lst-modules";
    }

    // --- API JSON (POUR LA MODALE JS) ---
    @GetMapping("/api/module/{id}")
    @ResponseBody
    public ResponseEntity<Module> getModuleJSON(@PathVariable Long id) {
        Module module = moduleService.getModuleById(id);
        if (module != null) {
            return ResponseEntity.ok(module);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- ARCHIVAGE ---
    @PostMapping("/archive-module/{id}")
    public String archiveModule(@PathVariable Long id, RedirectAttributes ra) {
        try {
            moduleService.archiveModule(id);
            ra.addFlashAttribute("success", "Module archivé.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur lors de l'archivage.");
        }
        return "redirect:/lst-modules";
    }

    // --- LISTE DES ARCHIVES ---
    @GetMapping("/lst-modules-archives")
    public String archives(Model model) {
        model.addAttribute("modules", moduleService.getArchivedModules());
        return "module-archived-list";
    }

    // --- DÉSARCHIVAGE (RESTAURATION) ---
    @PostMapping("/unarchive-module/{id}")
    public String unarchiveModule(@PathVariable Long id, RedirectAttributes ra) {
        try {
            moduleService.unarchiveModule(id);
            ra.addFlashAttribute("success", "Module restauré avec succès.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur lors de la restauration.");
        }
        return "redirect:/lst-modules-archives"; // On reste sur les archives pour voir le résultat (ou rediriger vers /lst-modules selon pref)
    }
}