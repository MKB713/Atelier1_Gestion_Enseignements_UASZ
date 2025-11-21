package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Formation;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FiliereService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FormationService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.NiveauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FormationController {

    @Autowired
    private FormationService formationService;

    @Autowired
    private FiliereService filiereService;

    @Autowired
    private NiveauService niveauService;

    // --- LISTE ---
    @GetMapping("/lst-formations")
    public String listFormations(@RequestParam(required = false) String archive, Model model) {
        // Si le paramètre archive est présent, on peut filtrer (à implémenter dans le service si besoin)
        // Pour l'instant, on affiche tout ou on filtre par statut selon ta logique
        model.addAttribute("formations", formationService.getAllFormations());
        return "lst-formations";
    }

    // --- FORMULAIRE AJOUT ---
    @GetMapping("/formations/ajouter")
    public String showAddForm(Model model) {
        model.addAttribute("formation", new Formation());
        model.addAttribute("filieres", filiereService.getAllFiliere());
        model.addAttribute("niveaux", niveauService.getAllNiveaux());
        model.addAttribute("titrePage", "Nouvelle Formation");
        return "form-formation";
    }

    // --- FORMULAIRE MODIFICATION ---
    @GetMapping("/formations/modifier/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Formation formation = formationService.getFormationById(id);
            model.addAttribute("formation", formation);
            model.addAttribute("filieres", filiereService.getAllFiliere());
            model.addAttribute("niveaux", niveauService.getAllNiveaux());
            model.addAttribute("titrePage", "Modifier la Formation");
            return "form-formation";
        } catch (Exception e) {
            return "redirect:/lst-formations";
        }
    }

    // --- SAUVEGARDE (Create & Update) ---
    @PostMapping("/save-formation")
    public String saveFormation(@ModelAttribute Formation formation, RedirectAttributes ra, Model model) {
        try {
            // La méthode save() du service redirige vers create ou update
            formationService.save(formation);
            ra.addFlashAttribute("success", "Formation enregistrée avec succès !");
            return "redirect:/lst-formations";
        } catch (IllegalArgumentException e) {
            // En cas d'erreur (code dupliqué, etc.), on recharge le formulaire avec l'erreur
            ra.addFlashAttribute("error", e.getMessage());
            // Astuce : Pour rediriger vers le bon formulaire (ajout ou modif)
            if(formation.getId() != null) {
                return "redirect:/formations/modifier/" + formation.getId();
            }
            return "redirect:/formations/ajouter";
        }
    }

    // --- ACTIONS : ARCHIVER ---
    @PatchMapping("/formations/{id}/archiver") // Utilisation de PATCH comme dans ton JS
    @ResponseBody // Important pour les appels fetch JS
    public org.springframework.http.ResponseEntity<?> archiverFormation(@PathVariable Long id) {
        try {
            formationService.archiveFormation(id);
            return org.springframework.http.ResponseEntity.ok("Formation archivée");
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- ACTIONS : ACTIVER ---
    @PatchMapping("/formations/{id}/activer") // Utilisation de PATCH comme dans ton JS
    @ResponseBody
    public org.springframework.http.ResponseEntity<?> activerFormation(@PathVariable Long id) {
        try {
            formationService.activerFormation(id);
            return org.springframework.http.ResponseEntity.ok("Formation activée");
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}