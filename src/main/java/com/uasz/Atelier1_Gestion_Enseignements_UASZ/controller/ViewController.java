package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.PlanningDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.SeanceDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EcService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EnseignantService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.PlanningService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.SalleService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.SeanceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ViewController {

    @Autowired
    private SeanceService seanceService;

    @Autowired
    private PlanningService planningService;

    @Autowired
    private EnseignantService enseignantService;

    @Autowired
    private SalleService salleService;

    @Autowired
    private EcService ecService;

    // --- Home Page (redirect to Seances list) ---
    @GetMapping("/lst-seances")
    public String home() {
        return "";
    }

    // --- Gestion des Emplois du Temps (List all Seances) ---
    @GetMapping("/seances")
    public String listAllSeances(Model model) {
        List<Seance> seances = seanceService.getAllSeances();
        List<SeanceDTO> seanceDTOs = seances.stream().map(seance -> {
            SeanceDTO dto = new SeanceDTO();
            dto.setId(seance.getId());
            dto.setDateSeance(seance.getDateSeance());
            dto.setHeureDebut(seance.getHeureDebut());
            dto.setHeureFin(seance.getHeureFin());
            dto.setEcNom(seance.getEc().getLibelle());
            dto.setEnseignantNom(seance.getEnseignant().getPrenom() + " " + seance.getEnseignant().getNom());
            dto.setSalleNom(seance.getSalle().getLibelle());
            return dto;
        }).collect(java.util.stream.Collectors.toList());
        model.addAttribute("seances", seanceDTOs);
        return "seance-list";
    }

    // --- US33: Récupérer les plannings des salles ---
    @GetMapping("/planning/salle/search")
    public String showPlanningBySalleSearchForm() {
        return "planning-salle-search";
    }

    @GetMapping("/planning/salle")
    public String getPlanningBySalle(@RequestParam("salleId") Long salleId, Model model, RedirectAttributes redirectAttributes) {
        try {
            PlanningDTO planning = planningService.getPlanningBySalle(salleId);
            model.addAttribute("planning", planning);
            return "planning-salle";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/planning/salle/search"; // Redirect to search page with error
        }
    }

    // --- US34: Ajouter une séance ---
    @GetMapping("/seances/add")
    public String showAddSeanceForm(Model model) {
        model.addAttribute("seanceDTO", new SeanceDTO());
        model.addAttribute("enseignants", enseignantService.getAllEnseignants()); // Assuming this method exists
        model.addAttribute("salles", salleService.getAllSalles()); // Assuming this method exists
        model.addAttribute("ecs", ecService.getAllEcs()); // Assuming this method exists
        return "seance-add";
    }

    // --- US35: Modifier une séance ---
    @GetMapping("/seances/edit/{id}")
    public String showEditSeanceForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Seance seance = seanceService.getSeanceById(id);
            if (seance == null) {
                throw new EntityNotFoundException("Séance non trouvée avec l'id: " + id);
            }
            // Map Seance entity to SeanceDTO for the form
            SeanceDTO seanceDTO = new SeanceDTO();
            seanceDTO.setId(seance.getId());
            seanceDTO.setDateSeance(seance.getDateSeance());
            seanceDTO.setHeureDebut(seance.getHeureDebut());
            seanceDTO.setHeureFin(seance.getHeureFin());
            seanceDTO.setEnseignantId(seance.getEnseignant().getId());
            seanceDTO.setSalleId(seance.getSalle().getId());
            seanceDTO.setEcId(seance.getEc().getId());

            model.addAttribute("seanceDTO", seanceDTO);
            model.addAttribute("enseignants", enseignantService.getAllEnseignants());
            model.addAttribute("salles", salleService.getAllSalles());
            model.addAttribute("ecs", ecService.getAllEcs());
            return "seance-edit"; // Correctly return the view name here
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/seances"; // Redirect to seances list on error
        }
    }

    // --- US36: Supprimer une séance ---
    @GetMapping("/seances/delete/{id}")
    public String showDeleteSeanceConfirmation(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (seanceService.getSeanceById(id) == null) {
                throw new EntityNotFoundException("Séance non trouvée avec l'id: " + id);
            }
            model.addAttribute("seanceId", id);
            return "seance-delete-confirm";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/seances"; // Redirect to seances list
        }
    }

    // --- US37: Rechercher et afficher les séances d’un enseignant ---
    @GetMapping("/seances/enseignant/search")
    public String showSeancesByEnseignant(@RequestParam(value = "enseignantId", required = false) Long enseignantId, Model model, RedirectAttributes redirectAttributes) {
        if (enseignantId != null) {
            try {
                // Assuming EnseignantService has a getEnseignantById method
                if (enseignantService.getEnseignantById(enseignantId) == null) {
                    throw new EntityNotFoundException("Enseignant non trouvé avec l'id: " + enseignantId);
                }
                List<Seance> seances = seanceService.getByEnseignant(enseignantId);
                // Map Seance entities to SeanceDTOs for display
                List<SeanceDTO> seanceDTOs = seances.stream().map(seance -> {
                    SeanceDTO dto = new SeanceDTO();
                    dto.setId(seance.getId());
                    dto.setDateSeance(seance.getDateSeance());
                    dto.setHeureDebut(seance.getHeureDebut());
                    dto.setHeureFin(seance.getHeureFin());
                    dto.setEcNom(seance.getEc().getLibelle());
                    dto.setEnseignantNom(seance.getEnseignant().getPrenom() + " " + seance.getEnseignant().getNom());
                    dto.setSalleNom(seance.getSalle().getLibelle());
                    return dto;
                }).collect(java.util.stream.Collectors.toList());
                model.addAttribute("seances", seanceDTOs);
            } catch (EntityNotFoundException e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return "redirect:/seances/enseignant/search"; // Redirect back to search page with error
            }
        }
        return "seance-enseignant-search";
    }

    // --- US34: Ajouter une séance (Form Submission) ---
    @PostMapping("/seances/save")
    public String saveSeance(@ModelAttribute SeanceDTO seanceDTO, RedirectAttributes redirectAttributes) {
        try {
            seanceService.createSeance(seanceDTO);
            redirectAttributes.addFlashAttribute("message", "Séance ajoutée avec succès !");
            return "redirect:/seances"; // Redirect to seances list
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout de la séance : " + e.getMessage());
            return "redirect:/seances/add";
        }
    }

    // --- US35: Modifier une séance (Form Submission) ---
    @PostMapping("/seances/update/{id}")
    public String updateSeance(@PathVariable("id") Long id, @ModelAttribute SeanceDTO seanceDTO, RedirectAttributes redirectAttributes) {
        try {
            seanceService.updateSeance(id, seanceDTO);
            redirectAttributes.addFlashAttribute("message", "Séance modifiée avec succès !");
            return "redirect:/seances"; // Redirect to seances list
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification de la séance : " + e.getMessage());
            return "redirect:/seances/edit/" + id;
        }
    }

    // --- US36: Supprimer une séance (Form Submission) ---
    @PostMapping("/seances/delete/{id}")
    public String deleteSeance(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            seanceService.deleteSeance(id);
            redirectAttributes.addFlashAttribute("message", "Séance supprimée avec succès !");
            return "redirect:/seances"; // Redirect to seances list
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
            return "redirect:/seances/delete/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur inattendue lors de la suppression : " + e.getMessage());
            return "redirect:/seances/delete/" + id;
        }
    }

    // --- Gestion des Salles ---
    @GetMapping("/salles")
    public String listAllSalles(Model model) {
        model.addAttribute("salles", salleService.getAllSalles());
        return "salle-list";
    }

    @GetMapping("/salles/add")
    public String showAddSalleForm(Model model) {
        model.addAttribute("salle", new com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Salle());
        return "salle-add-edit";
    }

    @GetMapping("/salles/edit/{id}")
    public String showEditSalleForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Salle salle = salleService.getSalleById(id);
            if (salle == null) {
                throw new EntityNotFoundException("Salle non trouvée avec l'id: " + id);
            }
            model.addAttribute("salle", salle);
            return "salle-add-edit";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/salles";
        }
    }

    @PostMapping("/salles/save")
    public String saveSalle(@ModelAttribute com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Salle salle, RedirectAttributes redirectAttributes) {
        try {
            if (salle.getId() == null) {
                salleService.createSalle(salle);
                redirectAttributes.addFlashAttribute("message", "Salle ajoutée avec succès !");
            } else {
                salleService.updateSalle(salle.getId(), salle);
                redirectAttributes.addFlashAttribute("message", "Salle modifiée avec succès !");
            }
            return "redirect:/salles";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement de la salle : " + e.getMessage());
            return "redirect:/salles/add"; // Or redirect to edit page if it was an update
        }
    }

    @GetMapping("/salles/delete/{id}")
    public String deleteSalle(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            salleService.deleteSalle(id);
            redirectAttributes.addFlashAttribute("message", "Salle supprimée avec succès !");
            return "redirect:/salles";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
            return "redirect:/salles";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur inattendue lors de la suppression : " + e.getMessage());
            return "redirect:/salles";
        }
    }
}
