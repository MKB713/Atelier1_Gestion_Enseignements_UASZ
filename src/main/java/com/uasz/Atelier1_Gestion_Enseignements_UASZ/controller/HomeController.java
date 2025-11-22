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

    @GetMapping("/welcom")
    public String index(Model model) {
        DashboardStatsDTO stats = dashboardService.getStats();
        model.addAttribute("stats", stats);
        return "index"; // Cela correspondra à index.html
    }
}