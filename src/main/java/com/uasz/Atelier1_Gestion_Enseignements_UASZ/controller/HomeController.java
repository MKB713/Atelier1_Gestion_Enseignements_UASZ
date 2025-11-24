package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.DashboardStatsDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Role;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.DashboardService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.FormationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private FormationService formationService;

    /**
     * Page d'accueil - Redirige vers le dashboard approprié selon le rôle
     */
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        Role userRole = (Role) session.getAttribute("userRole");

        // Si aucun rôle n'est défini, rediriger vers la sélection de rôle
        if (userRole == null) {
            return "redirect:/select-role";
        }

        // Rediriger vers le dashboard approprié selon le rôle
        switch (userRole) {
            case ETUDIANT:
                return "redirect:/dashboard/etudiant";
            case ENSEIGNANT:
                return "redirect:/dashboard/enseignant";
            case RESPONSABLE_MASTER:
                return "redirect:/dashboard/responsable";
            case COORDONATEUR_DES_LICENCES:
                return "redirect:/dashboard/coordinateur";
            case ADMIN:
            case CHEF_DE_DEPARTEMENT:
                return "redirect:/dashboard/admin";
            default:
                return "redirect:/select-role";
        }
    }

    /**
     * Dashboard ÉTUDIANT - Voir uniquement les emplois du temps de toutes les formations
     */
    @GetMapping("/dashboard/etudiant")
    public String dashboardEtudiant(HttpSession session, Model model) {
        Role userRole = (Role) session.getAttribute("userRole");
        if (userRole == null || userRole != Role.ETUDIANT) {
            return "redirect:/select-role";
        }

        model.addAttribute("formations", formationService.getAllFormations());
        model.addAttribute("userRole", userRole);
        return "dashboard-etudiant";
    }

    /**
     * Dashboard ENSEIGNANT - Consultation uniquement (formations, maquettes, pédagogies, cahier de texte, EDT)
     */
    @GetMapping("/dashboard/enseignant")
    public String dashboardEnseignant(HttpSession session, Model model) {
        Role userRole = (Role) session.getAttribute("userRole");
        if (userRole == null || userRole != Role.ENSEIGNANT) {
            return "redirect:/select-role";
        }

        model.addAttribute("formations", formationService.getAllFormations());
        model.addAttribute("userRole", userRole);
        return "dashboard-enseignant";
    }

    /**
     * Dashboard RESPONSABLE MASTER - Gestion complète des masters
     */
    @GetMapping("/dashboard/responsable")
    public String dashboardResponsable(HttpSession session, Model model) {
        Role userRole = (Role) session.getAttribute("userRole");
        if (userRole == null || userRole != Role.RESPONSABLE_MASTER) {
            return "redirect:/select-role";
        }

        DashboardStatsDTO stats = dashboardService.getStats();
        model.addAttribute("stats", stats);
        model.addAttribute("formations", formationService.getAllFormations());
        model.addAttribute("userRole", userRole);
        return "dashboard-responsable";
    }

    /**
     * Dashboard COORDINATEUR LICENCE - Gestion complète des licences
     */
    @GetMapping("/dashboard/coordinateur")
    public String dashboardCoordinateur(HttpSession session, Model model) {
        Role userRole = (Role) session.getAttribute("userRole");
        if (userRole == null || userRole != Role.COORDONATEUR_DES_LICENCES) {
            return "redirect:/select-role";
        }

        DashboardStatsDTO stats = dashboardService.getStats();
        model.addAttribute("stats", stats);
        model.addAttribute("formations", formationService.getAllFormations());
        model.addAttribute("userRole", userRole);
        return "dashboard-coordinateur";
    }

    /**
     * Dashboard ADMINISTRATEUR - Accès complet à tout
     */
    @GetMapping("/dashboard/admin")
    public String dashboardAdmin(HttpSession session, Model model) {
        Role userRole = (Role) session.getAttribute("userRole");
        if (userRole == null || userRole != Role.ADMIN) {
            return "redirect:/select-role";
        }

        DashboardStatsDTO stats = dashboardService.getStats();
        model.addAttribute("stats", stats);
        model.addAttribute("userRole", userRole);
        return "index"; // Utilise le dashboard admin existant (index.html)
    }
}
