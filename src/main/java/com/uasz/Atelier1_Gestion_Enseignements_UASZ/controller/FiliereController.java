package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Filiere;
import  org.springframework.beans.factory.annotation.Autowired;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FiliereService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class FiliereController {
    @Autowired
    private FiliereService filiereService;

    @RequestMapping("/addFiliere")
    public String addFiliere(Model model) {
        Filiere filiere = new Filiere();
        model.addAttribute("filere",filiere);
        return "filiere-add";
    }
    @RequestMapping("/save")
    public String save(Filiere filiere) {
        filiereService.addFiliere(filiere);
        return "redirect:/filiere-add";
    }
}
