package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Utilisateur;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/utilisateurs")
    public String listUtilisateurs(
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String q,
            Model model) {

        if ("ajout".equals(mode)) {
            model.addAttribute("utilisateur", new Utilisateur());
            model.addAttribute("mode", "ajout");
        }
        else if ("modification".equals(mode) && id != null) {
            try {
                Utilisateur utilisateur = utilisateurService.getUtilisateurById(id)
                        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                model.addAttribute("utilisateur", utilisateur);
                model.addAttribute("mode", "modification");
            } catch (RuntimeException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        else if ("recherche".equals(mode) && q != null && !q.trim().isEmpty()) {
            // Recherche simple par nom
            model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs().stream()
                    .filter(u -> u.getNom().toLowerCase().contains(q.toLowerCase()) ||
                            u.getPrenom().toLowerCase().contains(q.toLowerCase()) ||
                            u.getUsername().toLowerCase().contains(q.toLowerCase()))
                    .toList());
            model.addAttribute("query", q.trim());
            model.addAttribute("mode", "recherche");
        }
        else {
            model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());
            model.addAttribute("mode", "liste");
        }

        return "utilisateur";
    }

    @PostMapping("/save-utilisateur")
    public String saveUtilisateur(@ModelAttribute Utilisateur utilisateur, Model model) {
        try {
            utilisateurService.saveUtilisateur(utilisateur);
            return "redirect:/utilisateurs?success=Utilisateur créé avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("mode", "ajout");
            model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());
            return "utilisateur";
        }
    }

    @PostMapping("/update-utilisateur")
    public String updateUtilisateur(@ModelAttribute Utilisateur utilisateur, Model model) {
        try {
            utilisateurService.saveUtilisateur(utilisateur);
            return "redirect:/utilisateurs?success=Utilisateur modifié avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("mode", "modification");
            model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());
            return "utilisateur";
        }
    }

    @GetMapping("/delete-utilisateur/{id}")
    public String deleteUtilisateur(@PathVariable Long id, Model model) {
        try {
            utilisateurService.deleteUtilisateur(id);
            return "redirect:/utilisateurs?success=Utilisateur supprimé avec succès";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());
            model.addAttribute("mode", "liste");
            return "utilisateur";
        }
    }
}