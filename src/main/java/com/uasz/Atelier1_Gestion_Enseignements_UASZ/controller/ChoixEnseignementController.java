package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.ChoixEnseignementDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.ChoixEnseignement;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutChoix;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/choix-enseignement")
public class ChoixEnseignementController {

    @Autowired
    private ChoixEnseignementService choixService;

    @Autowired
    private EnseignantService enseignantService;

    @Autowired
    private UEService ueService;

    @Autowired
    private ECService ecService;


    // --- Liste des choix de l'enseignant connecté ---
    // Note: En production, récupérer l'ID depuis la session/sécurité
    @GetMapping("/mes-choix")
    public String mesChoix(@RequestParam(required = false) Long enseignantId, Model model) {
        if (enseignantId != null) {
            List<ChoixEnseignement> choix = choixService.getChoixByEnseignant(enseignantId);
            List<ChoixEnseignementDTO> choixDTOs = choix.stream()
                    .map(choixService::convertToDTO)
                    .collect(Collectors.toList());
            model.addAttribute("choix", choixDTOs);
            model.addAttribute("enseignantId", enseignantId);
        }
        model.addAttribute("enseignants", enseignantService.getAllEnseignants());
        return "choix-enseignement/mes-choix";
    }


    // ormulaire d'ajout d'un choix ---
    @GetMapping("/ajouter")
    public String showAddForm(@RequestParam(required = false) Long enseignantId, Model model) {
        model.addAttribute("choixDTO", new ChoixEnseignementDTO());
        model.addAttribute("enseignants", enseignantService.getAllEnseignants());
        model.addAttribute("ues", ueService.getAllUEs());
        model.addAttribute("ecs", ecService.getAllECs());
        model.addAttribute("semestres", List.of("S1", "S2", "S3", "S4", "S5", "S6"));
        if (enseignantId != null) {
            model.addAttribute("selectedEnseignantId", enseignantId);
        }
        return "choix-enseignement/form-choix";
    }

    // Soumission du formulaire d'ajout ---
    @PostMapping("/save")
    public String saveChoix(@ModelAttribute ChoixEnseignementDTO choixDTO,
                            RedirectAttributes redirectAttributes) {
        try {
            ChoixEnseignement saved = choixService.creerChoix(choixDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Votre choix d'enseignement a été soumis avec succès ! " +
                            "Il sera examiné par le chef de département.");
            return "redirect:/choix-enseignement/mes-choix?enseignantId=" + choixDTO.getEnseignantId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/choix-enseignement/ajouter?enseignantId=" + choixDTO.getEnseignantId();
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/choix-enseignement/ajouter";
        }
    }

    //  Formulaire de modification ---
    @GetMapping("/modifier/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            ChoixEnseignement choix = choixService.getChoixById(id);

            // Vérifier que le choix est modifiable
            if (choix.getStatut() != StatutChoix.EN_ATTENTE) {
                redirectAttributes.addFlashAttribute("error",
                        "Ce choix ne peut plus être modifié car il a été " +
                                choix.getStatut().getDisplayValue().toLowerCase());
                return "redirect:/choix-enseignement/mes-choix?enseignantId=" +
                        choix.getEnseignant().getId();
            }

            ChoixEnseignementDTO dto = choixService.convertToDTO(choix);
            model.addAttribute("choixDTO", dto);
            model.addAttribute("isEdit", true);
            model.addAttribute("ues", ueService.getAllUEs());
            model.addAttribute("ecs", ecService.getAllECs());
            model.addAttribute("semestres", List.of("S1", "S2", "S3", "S4", "S5", "S6"));
            return "choix-enseignement/form-choix";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/choix-enseignement/mes-choix";
        }
    }

    // Soumission de la modification ---
    @PostMapping("/update/{id}")
    public String updateChoix(@PathVariable Long id,
                              @ModelAttribute ChoixEnseignementDTO choixDTO,
                              RedirectAttributes redirectAttributes) {
        try {
            ChoixEnseignement updated = choixService.modifierChoix(id, choixDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Votre choix a été modifié avec succès. " +
                            "Le chef de département sera notifié de cette modification.");
            return "redirect:/choix-enseignement/mes-choix?enseignantId=" +
                    updated.getEnseignant().getId();
        } catch (IllegalStateException | EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/choix-enseignement/modifier/" + id;
        }
    }

    // Suppression d'un choix ---
    @PostMapping("/supprimer/{id}")
    public String deleteChoix(@PathVariable Long id,
                              @RequestParam Long enseignantId,
                              RedirectAttributes redirectAttributes) {
        try {
            choixService.supprimerChoix(id);
            redirectAttributes.addFlashAttribute("success",
                    "Votre choix a été supprimé avec succès.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Choix non trouvé.");
        }
        return "redirect:/choix-enseignement/mes-choix?enseignantId=" + enseignantId;
    }


    // --- Liste de tous les choix (vue admin) ---
    @GetMapping("/admin/liste")
    public String listeChoixAdmin(Model model) {
        List<ChoixEnseignement> tousLesChoix = choixService.getAllChoix();
        List<ChoixEnseignementDTO> choixDTOs = tousLesChoix.stream()
                .map(choixService::convertToDTO)
                .collect(Collectors.toList());
        model.addAttribute("choix", choixDTOs);
        model.addAttribute("statutChoix", StatutChoix.values());
        return "choix-enseignement/admin-liste";
    }

    // --- Liste des choix en attente ---
    @GetMapping("/admin/en-attente")
    public String listeChoixEnAttente(Model model) {
        List<ChoixEnseignement> choixEnAttente = choixService.getChoixEnAttente();
        List<ChoixEnseignementDTO> choixDTOs = choixEnAttente.stream()
                .map(choixService::convertToDTO)
                .collect(Collectors.toList());
        model.addAttribute("choix", choixDTOs);
        return "choix-enseignement/admin-en-attente";
    }

    //  Recherche par matricule ---
    @GetMapping("/admin/recherche")
    public String rechercheParMatricule(
            @RequestParam(required = false) Long matricule,
            Model model) {

        if (matricule != null) {
            List<ChoixEnseignementDTO> resultats = choixService.rechercherParMatricule(matricule);
            model.addAttribute("choix", resultats);
            model.addAttribute("matriculeRecherche", matricule);

            if (resultats.isEmpty()) {
                model.addAttribute("info",
                        "Aucun choix trouvé pour le matricule: " + matricule);
            }
        }
        return "choix-enseignement/admin-recherche";
    }

    // --- Validation d'un choix ---
    @PostMapping("/admin/valider/{id}")
    public String validerChoix(@PathVariable Long id,
                               @RequestParam(required = false) String commentaire,
                               RedirectAttributes redirectAttributes) {
        try {
            choixService.validerChoix(id, commentaire);
            redirectAttributes.addFlashAttribute("success",
                    "Le choix a été validé avec succès.");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/choix-enseignement/admin/en-attente";
    }

    // --- Rejet d'un choix ---
    @PostMapping("/admin/rejeter/{id}")
    public String rejeterChoix(@PathVariable Long id,
                               @RequestParam String commentaire,
                               RedirectAttributes redirectAttributes) {
        try {
            if (commentaire == null || commentaire.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error",
                        "Veuillez fournir un motif de rejet.");
                return "redirect:/choix-enseignement/admin/en-attente";
            }
            choixService.rejeterChoix(id, commentaire);
            redirectAttributes.addFlashAttribute("success",
                    "Le choix a été rejeté. L'enseignant sera notifié.");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/choix-enseignement/admin/en-attente";
    }

    // --- Détails d'un choix ---
    @GetMapping("/admin/details/{id}")
    public String detailsChoix(@PathVariable Long id, Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            ChoixEnseignement choix = choixService.getChoixById(id);
            model.addAttribute("choix", choixService.convertToDTO(choix));
            return "choix-enseignement/admin-details";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/choix-enseignement/admin/liste";
        }
    }
}