package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.ClasseDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Classe;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.ClasseService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FiliereService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.NiveauService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ClasseController {

    @Autowired
    private ClasseService classeService;

    @Autowired
    private FiliereService filiereService;

    @Autowired
    private NiveauService niveauService;

    /**
     * Affiche la liste de toutes les classes
     */
    @RequestMapping("/lst-classes")
    public String listClasses(Model model) {
        model.addAttribute("classes", classeService.getAllClasses());
        return "classe-list";
    }

    /**
     * Affiche le formulaire pour ajouter une classe
     */
    @RequestMapping("/add-classe")
    public String addClasse(Model model) {
        Classe classe = new Classe();
        model.addAttribute("classe", classe);
        model.addAttribute("filieres", filiereService.getAllFiliere());
        model.addAttribute("niveaux", niveauService.getAllNiveaux());
        return "classe-add";
    }

    /**
     * Enregistre une nouvelle classe
     */
    @PostMapping("/save-classe")
    public String saveClasse(@Valid @ModelAttribute("classe") Classe classe,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("filieres", filiereService.getAllFiliere());
            model.addAttribute("niveaux", niveauService.getAllNiveaux());
            return "classe-add";
        }

        try {
            ClasseDTO classeDTO = new ClasseDTO();
            classeDTO.setCode(classe.getCode());
            classeDTO.setLibelle(classe.getLibelle());
            classeDTO.setDescription(classe.getDescription());
            classeDTO.setAnneeAcademique(classe.getAnneeAcademique());
            classeDTO.setEffectifMax(classe.getEffectifMax());

            if (classe.getFiliere() != null) {
                classeDTO.setFiliereId(classe.getFiliere().getId());
            }
            if (classe.getNiveau() != null) {
                classeDTO.setNiveauId(classe.getNiveau().getId());
            }

            classeService.creerClasse(classeDTO);
            return "redirect:/lst-classes";
        } catch (IllegalArgumentException e) {
            model.addAttribute("codeError", e.getMessage());
            model.addAttribute("filieres", filiereService.getAllFiliere());
            model.addAttribute("niveaux", niveauService.getAllNiveaux());
            return "classe-add";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            model.addAttribute("error", "Erreur lors de l'enregistrement : violation de contrainte d'intégrité.");
            model.addAttribute("filieres", filiereService.getAllFiliere());
            model.addAttribute("niveaux", niveauService.getAllNiveaux());
            return "classe-add";
        }
    }

    /**
     * Affiche le formulaire pour modifier une classe
     */
    @GetMapping("/edit-classe/{id}")
    public String editClasse(@PathVariable Long id, Model model,
                             @RequestParam(required = false) String error) {
        Classe classe = classeService.getClasseById(id);
        model.addAttribute("classe", classe);
        model.addAttribute("filieres", filiereService.getAllFiliere());
        model.addAttribute("niveaux", niveauService.getAllNiveaux());
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "classe-edit";
    }

    /**
     * Met à jour une classe existante
     */
    @PostMapping("/update-classe")
    public String updateClasse(@RequestParam Long id,
                               @RequestParam(required = false) String code,
                               @RequestParam(required = false) String libelle,
                               @RequestParam(required = false) String description,
                               @RequestParam(required = false) Long filiereId,
                               @RequestParam(required = false) Long niveauId,
                               @RequestParam(required = false) String anneeAcademique,
                               @RequestParam(required = false) Integer effectifMax,
                               Model model) {
        try {
            ClasseDTO classeDTO = new ClasseDTO();
            classeDTO.setCode(code);
            classeDTO.setLibelle(libelle);
            classeDTO.setDescription(description);
            classeDTO.setFiliereId(filiereId);
            classeDTO.setNiveauId(niveauId);
            classeDTO.setAnneeAcademique(anneeAcademique);
            classeDTO.setEffectifMax(effectifMax);

            classeService.modifierClasse(id, classeDTO);
            return "redirect:/lst-classes";
        } catch (IllegalArgumentException e) {
            return "redirect:/edit-classe/" + id + "?error=" + e.getMessage();
        }
    }

    /**
     * Affiche les détails d'une classe
     */
    @GetMapping("/view-classe/{id}")
    public String viewClasse(@PathVariable Long id, Model model) {
        try {
            Classe classe = classeService.getClasseById(id);
            model.addAttribute("classe", classe);
            return "classe-detail";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la récupération des détails de la classe.");
            return "classe-detail";
        }
    }

    // ==================== REST API ENDPOINTS ====================

    /**
     * API - Récupère toutes les classes
     */
    @GetMapping("/api/classes")
    @ResponseBody
    public ResponseEntity<List<Classe>> getAllClasses() {
        List<Classe> classes = classeService.getAllClasses();
        return ResponseEntity.ok(classes);
    }

    /**
     * API - Récupère une classe par son ID
     */
    @GetMapping("/api/classes/{id}")
    @ResponseBody
    public ResponseEntity<?> getClasseById(@PathVariable Long id) {
        try {
            Classe classe = classeService.getClasseById(id);
            return ResponseEntity.ok(classe);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Classe non trouvée avec l'ID : " + id);
        }
    }

    /**
     * API - Crée une nouvelle classe
     */
    @PostMapping("/api/classes")
    @ResponseBody
    public ResponseEntity<?> createClasse(@Valid @RequestBody ClasseDTO classeDTO) {
        try {
            Classe classe = classeService.creerClasse(classeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(classe);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur de validation : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création de la classe : " + e.getMessage());
        }
    }

    /**
     * API - Met à jour une classe
     */
    @PutMapping("/api/classes/{id}")
    @ResponseBody
    public ResponseEntity<?> updateClasseRest(@PathVariable Long id,
                                               @Valid @RequestBody ClasseDTO classeDTO) {
        try {
            Classe classe = classeService.modifierClasse(id, classeDTO);
            return ResponseEntity.ok(classe);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur de validation : " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Classe non trouvée avec l'ID : " + id);
        }
    }

    /**
     * API - Archive une classe
     */
    @PatchMapping("/api/classes/{id}/archive")
    @ResponseBody
    public ResponseEntity<String> archiverClasse(@PathVariable Long id) {
        try {
            classeService.archiverClasse(id);
            return ResponseEntity.ok("La classe a été archivée avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Classe non trouvée avec l'ID : " + id);
        }
    }



    /**
     * API - Active une classe
     */
    @PostMapping("/activer-classe/{id}")
    @ResponseBody
    public ResponseEntity<String> activerClasse(@PathVariable Long id) {
        try {
            classeService.activerClasse(id);
            return ResponseEntity.ok("Classe activée avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API - Désactive une classe
     */
    @PostMapping("/desactiver-classe/{id}")
    @ResponseBody
    public ResponseEntity<String> desactiverClasse(@PathVariable Long id) {
        try {
            classeService.desactiverClasse(id);
            return ResponseEntity.ok("Classe désactivée avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Gère la recherche de classes par un terme (libellé ou code) et affiche les résultats.
     */
    @GetMapping("/classes/search") // Utilisez GET pour les recherches
    public String searchClasses(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Classe> classes;

        if (keyword != null && !keyword.isEmpty()) {
            // Utilise la méthode de recherche déjà implémentée dans ClasseService
            classes = classeService.rechercherClasses(keyword);
            model.addAttribute("keyword", keyword); // Pour conserver le terme recherché dans le formulaire
            model.addAttribute("message", classes.size() + " résultats trouvés pour '" + keyword + "'");
        } else {
            // Si pas de mot-clé, affiche toutes les classes
            classes = classeService.getAllClasses();
        }

        model.addAttribute("classes", classes);
        return "classe-list"; // Renvoie au template d'affichage de la liste
    }

    /**
     * API - Récupère les classes par filière
     */
    @GetMapping("/api/classes/filiere/{filiereId}")
    @ResponseBody
    public ResponseEntity<List<Classe>> getClassesByFiliere(@PathVariable Long filiereId) {
        List<Classe> classes = classeService.getClassesByFiliere(filiereId);
        return ResponseEntity.ok(classes);
    }

    /**
     * API - Récupère les classes par niveau
     */
    @GetMapping("/api/classes/niveau/{niveauId}")
    @ResponseBody
    public ResponseEntity<List<Classe>> getClassesByNiveau(@PathVariable Long niveauId) {
        List<Classe> classes = classeService.getClassesByNiveau(niveauId);
        return ResponseEntity.ok(classes);
    }

    /**
     * API - Récupère les classes actives
     */
    @GetMapping("/api/classes/actives")
    @ResponseBody
    public ResponseEntity<List<Classe>> getClassesActives() {
        List<Classe> classes = classeService.getClassesActives();
        return ResponseEntity.ok(classes);
    }

    /**
     * API - Récupère les classes archivées
     */
    @GetMapping("/api/classes/archivees")
    @ResponseBody
    public ResponseEntity<List<Classe>> getClassesArchivees() {
        List<Classe> classes = classeService.getClassesArchivees();
        return ResponseEntity.ok(classes);
    }
}
