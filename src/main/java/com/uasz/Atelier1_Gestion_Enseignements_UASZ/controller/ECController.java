package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.EC;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.ECService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.ModuleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ECController {

    @Autowired
    private ECService ecService;

    @Autowired
    private ModuleService moduleService;

    @RequestMapping(value = "/lst-ecs", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        List<EC> ecs;
        if (keyword != null && !keyword.isEmpty()) {
            ecs = ecService.searchECs(keyword);
        } else {
            ecs = ecService.getAllECs();
        }
        model.addAttribute("ecs", ecs);
        model.addAttribute("keyword", keyword);
        return "ec-list";
    }

    @GetMapping("/add-ec")
    public String addEC(Model model) {
        EC ec = new EC();
        model.addAttribute("ec", ec);
        model.addAttribute("modules", moduleService.getAllModules());
        return "ec-add";
    }

    @PostMapping("/save-ec")
    public String save(@Valid @ModelAttribute EC ec, BindingResult result, Model model) {
        // Si des erreurs de validation, revenir à la liste avec les erreurs
        if (result.hasErrors()) {
            model.addAttribute("ecs", ecService.getAllECs());
            model.addAttribute("ec", ec);
            model.addAttribute("modules", moduleService.getAllModules());
            return "ec-list";
        }
        
        if (ec.getId() != null) {
            ecService.updateEC(ec.getId(), ec);
        } else {
            ecService.addEC(ec);
        }
        return "redirect:/lst-ecs";
    }

    @PostMapping("/archive-ec/{id}")
    public String archiveEC(@PathVariable Long id) {
        ecService.archiveEC(id);
        return "redirect:/lst-ecs";
    }

    @GetMapping("/lst-ecs-archives")
    public String listeArchives(Model model) {
        model.addAttribute("ecs", ecService.getArchivedECs());
        return "ec-archived-list";
    }

    @PostMapping("/unarchive-ec/{id}")
    public String unarchiveEC(@PathVariable Long id) {
        ecService.unarchiveEC(id);
        return "redirect:/lst-ecs";
    }

    @GetMapping("/edit-ec/{id}")
    public String editEC(@PathVariable Long id, Model model) {
        EC ec = ecService.getECById(id);
        model.addAttribute("ec", ec);
        model.addAttribute("modules", moduleService.getAllModules());
        return "ec-add";
    }

    @PostMapping("/activate-ec/{id}")
    public String activateEC(@PathVariable Long id) {
        ecService.activateEC(id);
        return "redirect:/lst-ecs";
    }

    @PostMapping("/deactivate-ec/{id}")
    public String deactivateEC(@PathVariable Long id) {
        ecService.deactivateEC(id);
        return "redirect:/lst-ecs";
    }

    // ========== API REST pour les modales ==========

    @GetMapping("/api/ec/{id}")
    @ResponseBody
    public EC getECJSON(@PathVariable Long id) {
        return ecService.getECById(id);
    }

    @GetMapping("/api/modules")
    @ResponseBody
    public List<?> getModulesJSON() {
        return moduleService.getAllModules().stream()
                .map(m -> new Object() {
                    public Long id = m.getId();
                    public String libelle = m.getLibelle();
                }).toList();
    }
}