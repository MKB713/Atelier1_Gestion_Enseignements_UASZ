package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.EC;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.ECService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.ModuleService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.UEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ECController {

    @Autowired
    private ECService ecService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private UEService ueService; // AJOUT : Nécessaire pour la liste déroulante des UE

    // --- VUE PRINCIPALE (Liste + Modale) ---
    @GetMapping("/lst-ecs")
    public String listECs(Model model) {
        // 1. Liste des ECs pour le tableau
        model.addAttribute("ecs", ecService.getAllECs());

        // 2. Listes pour les select du formulaire
        model.addAttribute("modules", moduleService.getAllModules());
        model.addAttribute("ues", ueService.getAllUEs()); // AJOUT : Envoi des UE à la vue

        // 3. Objet vide pour le formulaire d'ajout
        model.addAttribute("ec", new EC());

        return "lst-ecs";
    }

    // --- VUE ARCHIVES ---
    @GetMapping("/lst-ecs-archives")
    public String listArchives(Model model) {
        model.addAttribute("ecs", ecService.getArchivedECs());
        return "ec-archived-list";
    }

    // --- ACTIONS CRUD (Ajout / Modif) ---
    @PostMapping("/save-ec")
    public String saveEC(@ModelAttribute EC ec, RedirectAttributes ra) {
        try {
            if (ec.getId() != null) {
                ecService.updateEC(ec.getId(), ec);
                ra.addFlashAttribute("success", "L'élément constitutif a été modifié avec succès.");
            } else {
                ecService.addEC(ec);
                ra.addFlashAttribute("success", "Nouvel EC ajouté avec succès.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/lst-ecs";
    }

    // --- API JSON (Pour remplir la modale en JS) ---
    @GetMapping("/api/ecs/{id}")
    @ResponseBody
    public ResponseEntity<EC> getEcJson(@PathVariable Long id) {
        EC ec = ecService.getECById(id);
        if (ec != null) {
            return ResponseEntity.ok(ec);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- GESTION DES ÉTATS (Activer / Désactiver / Archiver / Restaurer) ---

    @PostMapping("/activate-ec/{id}")
    public String activateEC(@PathVariable Long id, RedirectAttributes ra) {
        try {
            ecService.activateEC(id);
            ra.addFlashAttribute("success", "EC activé.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/lst-ecs";
    }

    @PostMapping("/deactivate-ec/{id}")
    public String deactivateEC(@PathVariable Long id, RedirectAttributes ra) {
        try {
            ecService.deactivateEC(id);
            ra.addFlashAttribute("success", "EC désactivé.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/lst-ecs";
    }

    @PostMapping("/archive-ec/{id}")
    public String archiveEC(@PathVariable Long id, RedirectAttributes ra) {
        try {
            ecService.archiveEC(id);
            ra.addFlashAttribute("success", "EC archivé.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/lst-ecs";
    }

    @PostMapping("/unarchive-ec/{id}")
    public String unarchiveEC(@PathVariable Long id, RedirectAttributes ra) {
        try {
            ecService.unarchiveEC(id);
            ra.addFlashAttribute("success", "EC restauré avec succès.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/lst-ecs"; // Retour à la liste principale après restauration
    }
}