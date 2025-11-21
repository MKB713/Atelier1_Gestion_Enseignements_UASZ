package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.NoteCahierTexteDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.NoteCahierTexte;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.ModuleRepository;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.CahierTextePdfService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EnseignantService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.NoteCahierTexteService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.SeanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class NoteCahierTexteController {

    @Autowired
    private NoteCahierTexteService noteCahierTexteService;

    @Autowired
    private SeanceService seanceService;

    @Autowired
    private EnseignantService enseignantService;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CahierTextePdfService cahierTextePdfService;

    /**
     * Affiche la liste de toutes les notes du cahier de texte
     */
    @RequestMapping("/lst-notes-cahier")
    public String listNotes(Model model,
                           @RequestParam(required = false) String success) {
        model.addAttribute("notes", noteCahierTexteService.getAllNotes());
        if (success != null) {
            model.addAttribute("success", success);
        }
        return "note-cahier-list";
    }

    /**
     * Affiche le formulaire pour ajouter une note
     */
    @GetMapping("/add-note-cahier")
    public String addNote(Model model) {
        NoteCahierTexte note = new NoteCahierTexte();
        model.addAttribute("note", note);
        model.addAttribute("seances", seanceService.getSeancesNonTerminees());
        return "note-cahier-add";
    }

    /**
     * Enregistre une nouvelle note
     */
    @PostMapping("/save-note-cahier")
    public String saveNote(@Valid @ModelAttribute("note") NoteCahierTexte note,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("seances", seanceService.getSeancesNonTerminees());
            return "note-cahier-add";
        }

        try {
            NoteCahierTexteDTO noteDTO = new NoteCahierTexteDTO();
            noteDTO.setTitre(note.getTitre());
            noteDTO.setContenu(note.getContenu());

            if (note.getSeance() != null) {
                noteDTO.setSeanceId(note.getSeance().getId());
            }

            noteDTO.setObjectifsPedagogiques(note.getObjectifsPedagogiques());
            noteDTO.setActivitesRealisees(note.getActivitesRealisees());
            noteDTO.setTravailDemande(note.getTravailDemande());
            noteDTO.setObservations(note.getObservations());

            if (note.getEnseignant() != null) {
                noteDTO.setEnseignantId(note.getEnseignant().getId());
            }

            noteCahierTexteService.ajouterNote(noteDTO);

            redirectAttributes.addAttribute("success", "Note ajoutée avec succès au cahier de texte !");
            return "redirect:/lst-notes-cahier";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("seances", seanceService.getSeancesNonTerminees());
            return "note-cahier-add";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
            model.addAttribute("seances", seanceService.getSeancesNonTerminees());
            return "note-cahier-add";
        }
    }

    /**
     * Affiche le formulaire pour modifier une note
     * CRITERE : Vérifie que la note n'est pas validée avant de permettre l'édition
     */
    @GetMapping("/edit-note-cahier/{id}")
    public String editNote(@PathVariable Long id, Model model,
                          @RequestParam(required = false) String error,
                          RedirectAttributes redirectAttributes) {
        NoteCahierTexte note = noteCahierTexteService.getNoteById(id);

        // RESTRICTION : Vérifier si la note peut être modifiée
        if (note.isEstValide()) {
            redirectAttributes.addAttribute("error", "Cette note a été validée et ne peut plus être modifiée.");
            return "redirect:/view-note-cahier/" + id;
        }

        model.addAttribute("note", note);
        model.addAttribute("seances", seanceService.getAllSeances());
        // Récupérer l'historique des modifications
        model.addAttribute("historique", noteCahierTexteService.getHistoriqueNote(id));
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "note-cahier-edit";
    }

    /**
     * Met à jour une note existante
     * CRITERE : Gère les erreurs si la note est validée
     */
    @PostMapping("/update-note-cahier")
    public String updateNote(@RequestParam Long id,
                            @RequestParam(required = false) String titre,
                            @RequestParam(required = false) String contenu,
                            @RequestParam(required = false) Long seanceId,
                            @RequestParam(required = false) String objectifsPedagogiques,
                            @RequestParam(required = false) String activitesRealisees,
                            @RequestParam(required = false) String travailDemande,
                            @RequestParam(required = false) String observations,
                            RedirectAttributes redirectAttributes) {
        try {
            NoteCahierTexteDTO noteDTO = new NoteCahierTexteDTO();
            noteDTO.setTitre(titre);
            noteDTO.setContenu(contenu);
            noteDTO.setSeanceId(seanceId);
            noteDTO.setObjectifsPedagogiques(objectifsPedagogiques);
            noteDTO.setActivitesRealisees(activitesRealisees);
            noteDTO.setTravailDemande(travailDemande);
            noteDTO.setObservations(observations);

            noteCahierTexteService.modifierNote(id, noteDTO);
            redirectAttributes.addAttribute("success", "Note modifiée avec succès ! Modifications enregistrées dans l'historique.");
            return "redirect:/lst-notes-cahier";
        } catch (IllegalStateException e) {
            // La note est validée et ne peut plus être modifiée
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/view-note-cahier/" + id;
        } catch (IllegalArgumentException e) {
            return "redirect:/edit-note-cahier/" + id + "?error=" + e.getMessage();
        }
    }

    /**
     * Affiche les détails d'une note
     */
    @GetMapping("/view-note-cahier/{id}")
    public String viewNote(@PathVariable Long id, Model model) {
        try {
            NoteCahierTexte note = noteCahierTexteService.getNoteById(id);
            model.addAttribute("note", note);
            return "note-cahier-detail";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la récupération des détails de la note.");
            return "note-cahier-detail";
        }
    }

    // ==================== REST API ENDPOINTS ====================

    /**
     * API - Récupère toutes les notes
     */
    @GetMapping("/api/notes-cahier")
    @ResponseBody
    public ResponseEntity<List<NoteCahierTexte>> getAllNotes() {
        List<NoteCahierTexte> notes = noteCahierTexteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }

    /**
     * API - Récupère une note par son ID
     */
    @GetMapping("/api/notes-cahier/{id}")
    @ResponseBody
    public ResponseEntity<?> getNoteById(@PathVariable Long id) {
        try {
            NoteCahierTexte note = noteCahierTexteService.getNoteById(id);
            return ResponseEntity.ok(note);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Note non trouvée avec l'ID : " + id);
        }
    }

    /**
     * API - Crée une nouvelle note
     */
    @PostMapping("/api/notes-cahier")
    @ResponseBody
    public ResponseEntity<?> createNote(@Valid @RequestBody NoteCahierTexteDTO noteDTO) {
        try {
            NoteCahierTexte note = noteCahierTexteService.ajouterNote(noteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(note);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur de validation : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création de la note : " + e.getMessage());
        }
    }

    /**
     * API - Met à jour une note
     */
    @PutMapping("/api/notes-cahier/{id}")
    @ResponseBody
    public ResponseEntity<?> updateNoteRest(@PathVariable Long id,
                                           @Valid @RequestBody NoteCahierTexteDTO noteDTO) {
        try {
            NoteCahierTexte note = noteCahierTexteService.modifierNote(id, noteDTO);
            return ResponseEntity.ok(note);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur de validation : " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Note non trouvée avec l'ID : " + id);
        }
    }

    /**
     * API - Valide une note
     */
    @PatchMapping("/api/notes-cahier/{id}/valider")
    @ResponseBody
    public ResponseEntity<String> validerNote(@PathVariable Long id) {
        try {
            noteCahierTexteService.validerNote(id);
            return ResponseEntity.ok("La note a été validée avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Note non trouvée avec l'ID : " + id);
        }
    }

    /**
     * API - Supprime une note
     */
    @DeleteMapping("/api/notes-cahier/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteNote(@PathVariable Long id) {
        try {
            noteCahierTexteService.supprimerNote(id);
            return ResponseEntity.ok("Note supprimée avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Note non trouvée avec l'ID : " + id);
        }
    }

    /**
     * API - Récupère les notes par séance
     */
    @GetMapping("/api/notes-cahier/seance/{seanceId}")
    @ResponseBody
    public ResponseEntity<List<NoteCahierTexte>> getNotesBySeance(@PathVariable Long seanceId) {
        List<NoteCahierTexte> notes = noteCahierTexteService.getNotesBySeance(seanceId);
        return ResponseEntity.ok(notes);
    }

    /**
     * API - Récupère les notes par classe
     */
    @GetMapping("/api/notes-cahier/classe/{classeId}")
    @ResponseBody
    public ResponseEntity<List<NoteCahierTexte>> getNotesByClasse(@PathVariable Long classeId) {
        List<NoteCahierTexte> notes = noteCahierTexteService.getNotesByClasse(classeId);
        return ResponseEntity.ok(notes);
    }

    /**
     * Affiche l'historique complet des modifications d'une note
     */
    @GetMapping("/historique-note/{id}")
    public String viewHistorique(@PathVariable Long id, Model model) {
        try {
            NoteCahierTexte note = noteCahierTexteService.getNoteById(id);
            model.addAttribute("note", note);
            model.addAttribute("historique", noteCahierTexteService.getHistoriqueNote(id));
            return "note-cahier-historique";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la récupération de l'historique.");
            return "note-cahier-historique";
        }
    }

    /**
     * API - Récupère l'historique des modifications d'une note
     */
    @GetMapping("/api/notes-cahier/{id}/historique")
    @ResponseBody
    public ResponseEntity<?> getHistoriqueNote(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(noteCahierTexteService.getHistoriqueNote(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Note non trouvée avec l'ID : " + id);
        }
    }

    /**
     * Consulter le cahier de texte avec filtres (pour chef de département)
     */
    @GetMapping("/consulter-cahier-texte")
    public String consulterCahierTexte(Model model,
                                       @RequestParam(required = false) Long enseignantId,
                                       @RequestParam(required = false) Long moduleId,
                                       @RequestParam(required = false) Long semestre) {
        // Récupérer les notes avec filtres
        model.addAttribute("notes", noteCahierTexteService.consulterCahierTexte(enseignantId, moduleId, semestre));

        // Données pour les filtres
        model.addAttribute("enseignants", enseignantService.getAllEnseignants());
        model.addAttribute("modules", moduleRepository.findAll());

        // Conserver les valeurs des filtres sélectionnés
        model.addAttribute("selectedEnseignantId", enseignantId);
        model.addAttribute("selectedModuleId", moduleId);
        model.addAttribute("selectedSemestre", semestre);

        return "note-cahier-consultation";
    }

    /**
     * Génère et télécharge le PDF du cahier de texte
     */
    @GetMapping("/export-cahier-texte-pdf")
    public ResponseEntity<byte[]> exporterPdf(@RequestParam(required = false) Long enseignantId) {
        try {
            byte[] pdfBytes = cahierTextePdfService.genererPdfCahierTexte(enseignantId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "cahier-de-texte.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
