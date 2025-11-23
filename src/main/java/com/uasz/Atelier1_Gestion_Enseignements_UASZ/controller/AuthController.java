package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    /**
     * Page de sélection du rôle (simulation de login)
     */
    @GetMapping("/select-role")
    public String selectRole(Model model, HttpSession session) {
        model.addAttribute("roles", Role.values());
        model.addAttribute("currentRole", session.getAttribute("userRole"));
        return "select-role";
    }

    /**
     * Définir le rôle dans la session
     */
    @PostMapping("/set-role")
    public String setRole(@RequestParam("role") Role role, HttpSession session) {
        session.setAttribute("userRole", role);
        return "redirect:/";
    }

    /**
     * Déconnexion (clear session)
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/select-role";
    }
}
