package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.PlanningDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.SeanceDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Salle;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.*;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.BatimentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

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
    private RepartitionService repartitionService;

    @Autowired
    private EmploiService emploiService;

    @Autowired
    private BatimentRepository batimentRepository;

    // --- Home Page (redirect to Seances list) ---
    @GetMapping("/")
    public String home() {
        return "redirect:/seances";
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
            if (seance.getRepartition() != null && seance.getRepartition().getEc() != null) {
                dto.setEcLibelle(seance.getRepartition().getEc().getLibelle());
            }
            if (seance.getRepartition() != null && seance.getRepartition().getEnseignant() != null) {
                dto.setEnseignantNom(seance.getRepartition().getEnseignant().getPrenom() + " " + seance.getRepartition().getEnseignant().getNom());
            }
            if (seance.getSalle() != null) {
                dto.setSalleNom(seance.getSalle().getLibelle());
                if (seance.getSalle().getBatiment() != null) {
                    dto.setBatimentNom(seance.getSalle().getBatiment().getLibelle());
                }
            }
            return dto;
        }).collect(Collectors.toList());
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
            return "redirect:/planning/salle/search";
        }
    }

    // --- US34: Ajouter une séance ---
    @GetMapping("/seances/add")
    public String showAddSeanceForm(Model model) {
        model.addAttribute("seanceDTO", new SeanceDTO());
        model.addAttribute("salles", salleService.getAllSalles());
        model.addAttribute("emplois", emploiService.getAllEmplois());
        model.addAttribute("repartitions", repartitionService.getAllRepartitions());
        return "seance-add";
    }

    // --- US35: Modifier une séance ---
    @GetMapping("/seances/edit/{id}")
    public String showEditSeanceForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Seance seance = seanceService.getSeanceById(id);
            SeanceDTO seanceDTO = new SeanceDTO();
            seanceDTO.setId(seance.getId());
            seanceDTO.setDateSeance(seance.getDateSeance());
            seanceDTO.setHeureDebut(seance.getHeureDebut());
            seanceDTO.setHeureFin(seance.getHeureFin());
            seanceDTO.setDuree(seance.getDuree());
            seanceDTO.setSalleId(seance.getSalle() != null ? seance.getSalle().getId() : null);
            seanceDTO.setEmploiId(seance.getEmploi() != null ? seance.getEmploi().getId() : null);
            seanceDTO.setRepartitionId(seance.getRepartition() != null ? seance.getRepartition().getId() : null);

            model.addAttribute("seanceDTO", seanceDTO);
            model.addAttribute("salles", salleService.getAllSalles());
            model.addAttribute("emplois", emploiService.getAllEmplois());
            model.addAttribute("repartitions", repartitionService.getAllRepartitions());
            return "seance-edit";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/seances";
        }
    }

    // --- US36: Supprimer une séance ---
    @GetMapping("/seances/delete/{id}")
    public String showDeleteSeanceConfirmation(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            seanceService.getSeanceById(id); // Check if it exists
            model.addAttribute("seanceId", id);
            return "seance-delete-confirm";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/seances";
        }
    }

    // --- US37: Rechercher et afficher les séances d’un enseignant ---
    @GetMapping("/seances/enseignant/search")
    public String showSeancesByEnseignant(@RequestParam(value = "enseignantId", required = false) Long enseignantId, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("enseignants", enseignantService.getAllEnseignants());
        if (enseignantId != null) {
            try {
                if (enseignantService.getEnseignantById(enseignantId) == null) {
                    throw new EntityNotFoundException("Enseignant non trouvé avec l'id: " + enseignantId);
                }
                List<Seance> seances = seanceService.getByEnseignant(enseignantId);
                List<SeanceDTO> seanceDTOs = seances.stream().map(seance -> {
                    SeanceDTO dto = new SeanceDTO();
                    dto.setId(seance.getId());
                    dto.setDateSeance(seance.getDateSeance());
                    dto.setHeureDebut(seance.getHeureDebut());
                    dto.setHeureFin(seance.getHeureFin());
                    if (seance.getRepartition() != null && seance.getRepartition().getEc() != null) {
                        dto.setEcLibelle(seance.getRepartition().getEc().getLibelle());
                    }
                    if (seance.getRepartition() != null && seance.getRepartition().getEnseignant() != null) {
                        dto.setEnseignantNom(seance.getRepartition().getEnseignant().getPrenom() + " " + seance.getRepartition().getEnseignant().getNom());
                    }
                    if (seance.getSalle() != null) {
                        dto.setSalleNom(seance.getSalle().getLibelle());
                    }
                    return dto;
                }).collect(Collectors.toList());
                model.addAttribute("seances", seanceDTOs);
            } catch (EntityNotFoundException e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return "redirect:/seances/enseignant/search";
            }
        }
        return "seance-enseignant-search";
    }

    // --- Form Submission Handlers ---
    @PostMapping("/seances/save")
    public String saveSeance(@ModelAttribute SeanceDTO seanceDTO, RedirectAttributes redirectAttributes) {
        try {
            seanceService.createSeance(seanceDTO);
            redirectAttributes.addFlashAttribute("message", "Séance ajoutée avec succès !");
            return "redirect:/seances";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout de la séance : " + e.getMessage());
            return "redirect:/seances/add";
        }
    }

    @PostMapping("/seances/update/{id}")
    public String updateSeance(@PathVariable("id") Long id, @ModelAttribute SeanceDTO seanceDTO, RedirectAttributes redirectAttributes) {
        try {
            seanceService.updateSeance(id, seanceDTO);
            redirectAttributes.addFlashAttribute("message", "Séance modifiée avec succès !");
            return "redirect:/seances";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification de la séance : " + e.getMessage());
            return "redirect:/seances/edit/" + id;
        }
    }

    @PostMapping("/seances/delete/{id}")
    public String deleteSeance(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            seanceService.deleteSeance(id);
            redirectAttributes.addFlashAttribute("message", "Séance supprimée avec succès !");
            return "redirect:/seances";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
            return "redirect:/seances";
        }
    }

    // --- UC05: Rechercher les séances d’une semaine ---
    @GetMapping("/planning-hebdomadaire")
    public String showWeeklySchedule(@RequestParam(name = "date", required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate date, Model model) {
        java.time.LocalDate today = (date == null) ? java.time.LocalDate.now() : date;
        List<Seance> seances = seanceService.getSeancesByWeek(today);

        // Group seances by day of the week
        java.util.Map<java.time.DayOfWeek, List<Seance>> schedule = seances.stream()
                .collect(java.util.stream.Collectors.groupingBy(seance -> seance.getDateSeance().getDayOfWeek()));

        model.addAttribute("schedule", schedule);
        model.addAttribute("weekStartDate", today.with(java.time.DayOfWeek.MONDAY));
        model.addAttribute("previousDate", today.minusWeeks(1));
        model.addAttribute("nextDate", today.plusWeeks(1));
        model.addAttribute("daysOfWeek", new java.time.DayOfWeek[]{
                java.time.DayOfWeek.MONDAY, java.time.DayOfWeek.TUESDAY, java.time.DayOfWeek.WEDNESDAY,
                java.time.DayOfWeek.THURSDAY, java.time.DayOfWeek.FRIDAY, java.time.DayOfWeek.SATURDAY
        });

        return "planning-hebdomadaire";
    }

    // --- Enregistrer une Salle ---
    @PostMapping("/salles/save")
    public String saveSalle(@ModelAttribute Salle salle, RedirectAttributes redirectAttributes) {
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
            return "redirect:/salles/add"; // Redirect back to the form on error
        }
    }

    // --- Ajouter une Salle ---
    @GetMapping("/salles/add")
    public String showAddSalleForm(Model model) {
        model.addAttribute("salle", new Salle());
        model.addAttribute("batiments", batimentRepository.findAll());
        return "salle-add-edit";
    }

    // --- Gestion des Salles ---
    @GetMapping("/salles")
    public String showSalleList(Model model) {
        model.addAttribute("salles", salleService.getAllSalles());
        return "salle-list";
    }

    // Other existing methods for Salles etc. can remain as they are
    // ...
}

