package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.UserDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.CustomUserDetails;
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
    private final GlobalController globalController;

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
            CustomUserDetails currentUser = globalController.getCurrentUser();

            if (currentUser != null) {
                session.setAttribute("currentUser", currentUser);
                for (var authority : currentUser.getAuthorities()) {
                    String role = authority.getAuthority();
                    if ("ROLE_ADMIN".equals(role)) {
                        return "redirect:/dash-coordinateur";
                    }
                    else if ("ROLE_RESPONSABLE_MASTER".equals(role)) {
                        return "redirect:/dash-enseignant";
                    } else if ("ROLE_ETUDIANT".equals(role)) {
                        return "redirect:/dash-etudiant";
                    } else if ("ROLE_COORDONATEUR_DES_LICENCES".equals(role)) {
                        return "redirect:/dash-coordinateur";
                    }
                    else if ("CHEF_DE_DEPARTEMENT".equals(role)) {
                        return "redirect:/lst-enseignants";
                    }
                }

            }
            model.addAttribute("errorMessage", "Rôle non reconnu");
            return "login";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Email ou mot de passe incorrect");
            return "login";
        }
    }
}