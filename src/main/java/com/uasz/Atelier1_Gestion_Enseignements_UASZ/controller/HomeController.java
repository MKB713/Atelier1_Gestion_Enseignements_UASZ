package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.DashboardStatsDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * Page d'accueil (Tableau de Bord)
     * URL : http://localhost:8080/
     */
    @GetMapping("/")
    public String index(Model model) {
        // Récupération des statistiques pour les graphiques et compteurs
        DashboardStatsDTO stats = dashboardService.getStats();
        model.addAttribute("stats", stats);

        return "index"; // Correspond à src/main/resources/templates/index.html
    }
}