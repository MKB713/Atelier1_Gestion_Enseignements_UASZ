package com.uasz.Atelier1_Gestion_Enseignements_UASZ.config;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Role;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Role userRole = userDetails.getRole();

        String redirectUrl = "/";

        switch (userRole) {
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

        System.out.println("Connexion réussie - Rôle: " + userRole + " - Redirection vers: " + redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
