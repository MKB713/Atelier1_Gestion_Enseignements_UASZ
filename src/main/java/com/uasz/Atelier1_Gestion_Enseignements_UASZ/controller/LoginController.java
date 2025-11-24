package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.UserDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String index() {
        return "login";
    }

    @PostMapping("/auth2")
    public String signIn(Model model, UserDTO userDTO, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            // Récupérer le rôle de l'utilisateur connecté
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String redirectUrl = "/";

            switch (userDetails.getRole()) {
                case ETUDIANT:
                    redirectUrl = "/dashboard/etudiant";
                    break;
                case ENSEIGNANT:
                    redirectUrl = "/dashboard/enseignant";
                    break;
                case RESPONSABLE_MASTER:
                    redirectUrl = "/dashboard/responsable";
                    break;
                case COORDONATEUR_DES_LICENCES:
                    redirectUrl = "/dashboard/coordinateur";
                    break;
                case ADMIN:
                case CHEF_DE_DEPARTEMENT:
                    redirectUrl = "/dashboard/admin";
                    break;
            }

            System.out.println("Connexion réussie - Rôle: " + userDetails.getRole() + " - Redirection vers: " + redirectUrl);
            return "redirect:" + redirectUrl;

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Email ou mot de passe incorrect");
            return "login";
        }
    }
}
