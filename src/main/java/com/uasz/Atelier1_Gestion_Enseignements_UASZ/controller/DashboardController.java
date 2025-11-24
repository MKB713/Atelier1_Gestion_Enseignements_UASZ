package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @GetMapping("/dash-responsable")
    public String dashboardResponsable(Model model ) {
        return "dashboard-responsable";
    }
    @GetMapping("/dash-coordinateur")
    public String dashboardCoordinateur(Model model ) {
        return "dashboard-coordinateur";
    }
    @GetMapping("/dash-enseignant")
    public String dashboardEnseignant(Model model ) {
        return "dashboard-enseignant";
    }
    @GetMapping("/dash-etudiant")
    public String dashboardEtudiant(Model model ) {
        return "dashboard-etudiant";
    }
}
