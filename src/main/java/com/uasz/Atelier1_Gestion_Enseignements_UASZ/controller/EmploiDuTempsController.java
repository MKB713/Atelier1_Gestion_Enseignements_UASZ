package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.SeanceDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Role;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/emploi-du-temps")
public class EmploiDuTempsController {

    @Autowired
    private SeanceService seanceService;

    @Autowired
    private PlanningService planningService;

    @Autowired
    private PDFExportService pdfExportService;

    @Autowired
    private EnseignantService enseignantService;

    @Autowired
    private SalleService salleService;

    @Autowired
    private EcService ecService;

    /**
     * Page principale de l'emploi du temps - Vue calendrier hebdomadaire
     * Accessible à tous les rôles (Étudiant, Enseignant, Responsable, Coordinateur, Admin)
     */
    @GetMapping
    public String showEmploiDuTemps(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpSession session,
            Model model) {

        Role userRole = (Role) session.getAttribute("userRole");
        if (userRole == null) {
            return "redirect:/select-role";
        }

        LocalDate currentDate = (date != null) ? date : LocalDate.now();
        List<Seance> seances = planningService.getSeancesSemaine(currentDate);

        // Générer les heures uniques pour l'affichage
        java.util.Set<String> heuresUniques = new java.util.TreeSet<>();
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
        for (Seance seance : seances) {
            String heure = seance.getHeureDebut().format(timeFormatter) + " - " + seance.getHeureFin().format(timeFormatter);
            heuresUniques.add(heure);
        }

        // Préparer les dates de la semaine (lundi à samedi)
        LocalDate lundi = currentDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate samedi = currentDate.with(java.time.DayOfWeek.SATURDAY);
        java.util.List<LocalDate> joursVus = new java.util.ArrayList<>();
        for (int i = 0; i < 6; i++) {
            joursVus.add(lundi.plusDays(i));
        }

        model.addAttribute("seances", seances);
        model.addAttribute("currentDate", currentDate);
        model.addAttribute("userRole", userRole);
        model.addAttribute("heuresUniques", heuresUniques);
        model.addAttribute("dateDebut", lundi);
        model.addAttribute("dateFin", samedi);
        model.addAttribute("joursVus", joursVus);

        return "emploi-du-temps";
    }

    /**
     * US34 - Ajouter une séance (Responsable Master / Coordinateur Licence uniquement)
     */
    @GetMapping("/add-seance")
    public String showAddSeanceForm(HttpSession session, Model model) {
        Role userRole = (Role) session.getAttribute("userRole");

        // Vérifier les permissions
        if (userRole == null || userRole == Role.ETUDIANT || userRole == Role.ENSEIGNANT) {
            return "redirect:/emploi-du-temps?error=unauthorized";
        }

        model.addAttribute("seanceDTO", new SeanceDTO());
        model.addAttribute("enseignants", enseignantService.getAllEnseignants());
        model.addAttribute("salles", salleService.getAllSalles());
        model.addAttribute("ecs", ecService.getAllEcs());

        return "seance-add";
    }

    /**
     * US34 - Créer une séance
     */
    @PostMapping("/save-seance")
    public String saveSeance(@ModelAttribute SeanceDTO seanceDTO,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Role userRole = (Role) session.getAttribute("userRole");

        if (userRole == null || userRole == Role.ETUDIANT || userRole == Role.ENSEIGNANT) {
            return "redirect:/emploi-du-temps?error=unauthorized";
        }

        try {
            seanceService.createSeance(seanceDTO);
            redirectAttributes.addFlashAttribute("success", "Séance créée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/emploi-du-temps/add-seance";
        }

        return "redirect:/emploi-du-temps";
    }

    /**
     * US35 - Modifier une séance (Responsable Master / Coordinateur Licence uniquement)
     */
    @GetMapping("/edit-seance/{id}")
    public String showEditSeanceForm(@PathVariable Long id, HttpSession session, Model model) {
        Role userRole = (Role) session.getAttribute("userRole");

        if (userRole == null || userRole == Role.ETUDIANT || userRole == Role.ENSEIGNANT) {
            return "redirect:/emploi-du-temps?error=unauthorized";
        }

        Seance seance = seanceService.getSeanceById(id);
        if (seance == null) {
            return "redirect:/emploi-du-temps?error=notfound";
        }

        // Convertir Seance en SeanceDTO pour le formulaire
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

        return "seance-edit";
    }

    /**
     * US35 - Mettre à jour une séance
     */
    @PostMapping("/update-seance/{id}")
    public String updateSeance(@PathVariable Long id,
                               @ModelAttribute SeanceDTO seanceDTO,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Role userRole = (Role) session.getAttribute("userRole");

        if (userRole == null || userRole == Role.ETUDIANT || userRole == Role.ENSEIGNANT) {
            return "redirect:/emploi-du-temps?error=unauthorized";
        }

        try {
            seanceService.updateSeance(id, seanceDTO);
            redirectAttributes.addFlashAttribute("success", "Séance modifiée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/emploi-du-temps/edit-seance/" + id;
        }

        return "redirect:/emploi-du-temps";
    }

    /**
     * US36 - Supprimer une séance (Responsable Master / Coordinateur Licence uniquement)
     */
    @PostMapping("/delete-seance/{id}")
    public String deleteSeance(@PathVariable Long id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Role userRole = (Role) session.getAttribute("userRole");

        if (userRole == null || userRole == Role.ETUDIANT || userRole == Role.ENSEIGNANT) {
            return "redirect:/emploi-du-temps?error=unauthorized";
        }

        try {
            seanceService.deleteSeance(id);
            redirectAttributes.addFlashAttribute("success", "Séance supprimée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/emploi-du-temps";
    }

    /**
     * US38 - Exporter l'emploi du temps en PDF (semaine)
     */
    @GetMapping("/export-pdf-semaine")
    public ResponseEntity<byte[]> exportPDFSemaine(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpSession session) {

        Role userRole = (Role) session.getAttribute("userRole");
        if (userRole == null) {
            return ResponseEntity.status(403).build();
        }

        LocalDate currentDate = (date != null) ? date : LocalDate.now();
        List<Seance> seances = planningService.getSeancesSemaine(currentDate);

        byte[] pdfBytes = pdfExportService.generateEmploiDuTempsPDFSemaine(
                seances,
                currentDate,
                "Emploi du Temps - UASZ"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "emploi-du-temps-semaine.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    /**
     * US33 - Voir les séances d'une salle
     */
    @GetMapping("/salle/{id}")
    public String showPlanningSalle(@PathVariable Long id,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
                                    Model model) {
        LocalDate debut = (dateDebut != null) ? dateDebut : LocalDate.now();
        LocalDate fin = (dateFin != null) ? dateFin : LocalDate.now().plusMonths(1);

        List<Seance> seances = planningService.getPlanningSalleByPeriode(id, debut, fin);

        model.addAttribute("seances", seances);
        model.addAttribute("salleId", id);
        model.addAttribute("dateDebut", debut);
        model.addAttribute("dateFin", fin);

        return "planning-salle";
    }
}
