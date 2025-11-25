package com.uasz.Atelier1_Gestion_Enseignements_UASZ.config;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Role;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Contrôleur global qui ajoute automatiquement des attributs communs à tous les modèles
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * Ajoute automatiquement le rôle de l'utilisateur connecté au Model
     * Cela permet d'accéder à ${userRole} dans tous les templates Thymeleaf
     */
    @ModelAttribute
    public void addUserRoleToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
            && !authentication.getPrincipal().equals("anonymousUser")) {

            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) principal;
                Role userRole = userDetails.getRole();
                model.addAttribute("userRole", userRole);
            }
        }
    }
}
