package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.UE;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.UEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UEController {

    @Autowired
    private UEService ueService;

    // --- PAGE PRINCIPALE ---
    @GetMapping("/lst-ues")
    public String index(Model model) {
        model.addAttribute("ues", ueService.getAllUEs());
        model.addAttribute("ue", new UE()); // Pour le formulaire vide
        return "lst-ues";
    }

    // --- SAUVEGARDE ---
    @PostMapping("/save-ue")
    public String save(@ModelAttribute UE ue, RedirectAttributes ra) {
        try {
            ueService.saveUE(ue);
            ra.addFlashAttribute("success", ue.getId() != null ? "UE modifiée." : "Nouvelle UE ajoutée.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/lst-ues";
    }

    // --- API JSON (Pour Modale) ---
    @GetMapping("/api/ues/{id}")
    @ResponseBody
    public ResponseEntity<UE> getUEJson(@PathVariable Long id) {
        return ResponseEntity.ok(ueService.getUEById(id));
    }

    // --- ACTIONS ---
    @PostMapping("/ues/archiver/{id}")
    public String archiver(@PathVariable Long id, RedirectAttributes ra) {
        ueService.archiver(id);
        ra.addFlashAttribute("success", "UE archivée.");
        return "redirect:/lst-ues";
    }

    @PostMapping("/ues/activer/{id}")
    public String activer(@PathVariable Long id, RedirectAttributes ra) {
        ueService.activer(id);
        return "redirect:/lst-ues";
    }

    @PostMapping("/ues/desactiver/{id}")
    public String desactiver(@PathVariable Long id, RedirectAttributes ra) {
        ueService.desactiver(id);
        return "redirect:/lst-ues";
    }

    // --- PAGE ARCHIVES ---
    @GetMapping("/lst-ues-archives")
    public String archives(Model model) {
        model.addAttribute("ues", ueService.getArchivedUEs());
        return "ue-archived-list";
    }

    @PostMapping("/ues/restaurer/{id}")
    public String restaurer(@PathVariable Long id, RedirectAttributes ra) {
        ueService.restaurer(id);
        ra.addFlashAttribute("success", "UE restaurée.");
        return "redirect:/lst-ues-archives";
    }
}