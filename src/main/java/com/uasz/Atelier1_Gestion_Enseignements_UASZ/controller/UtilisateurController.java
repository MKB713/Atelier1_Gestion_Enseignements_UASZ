package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Utilisateur;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Role;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.CustomUserDetails;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.UtilisateurService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
//@RestController
@RequiredArgsConstructor
public class UtilisateurController {
    private final UtilisateurService utilisateurService;
    private final GlobalController globalController;
    @GetMapping("/lst-utilisateurs")
    public String listeUtilisateurs(Model model){
        Utilisateur currentUser = globalController.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("utilisateurs", utilisateurService.findAll(currentUser));
        } else {
            model.addAttribute("utilisateurs", List.of());
        }
        return "utilisateur-list";
    }


    @GetMapping("/parametres")
    public String settingsPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() ) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            System.out.println("Nom de l'utilisateur : " + userDetails.getUtilisateur().getNom());
            System.out.println("Prénom de l'utilisateur : " + userDetails.getUtilisateur().getPrenom());
            System.out.println("ROle :" + userDetails.getUtilisateur().getRole());
            model.addAttribute("utilisateur", userDetails.getUtilisateur());
            return "parametres";
        }

        return "redirect:/login";
    }



    @PostMapping("/ajout-user")
    public String createUser(Model model , Utilisateur utilisateur){
        System.out.println("METHODE APPELEE");
        try {
            utilisateurService.createUser(utilisateur);
            System.out.println("ajout nice utilisateur");
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("success", "Utilisateur ajouté avec succès");
            return "redirect:/lst-utilisateurs";
        }
        catch (Exception e) {
            System.out.println("Erreur ajout<UNK> utilisateur");
            model.addAttribute("errorMessage", "Problème lors de l'ajout ");
            return "utilisateur-list";
        }


    }
    @PostMapping("/edit/utilisateur/{id}")
    public  String editUser(Model model , Utilisateur utilisateur , @PathVariable int id){

            try {
               Utilisateur result = utilisateurService.updateUser(utilisateur);
                model.addAttribute("success", "Utilisateur modifié avec succès");
                return "redirect:/lst-utilisateurs";
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("error","Erreur survenue lors de la modification");
                return "utilisateur-list";
            }


    }
    @PostMapping("/edit1/utilisateur/{id}")
    public  String editUser1(Model model , Utilisateur utilisateur , @PathVariable int id){

        try {
            Utilisateur result = utilisateurService.updateUser(utilisateur);
            model.addAttribute("success", "Utilisateur modifié avec succès");
            return "redirect:/parametres";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage","Erreur survenue lors de la modification");
            return "utilisateur-list";
        }


    }


    @PostMapping("/archiver/{id}")
    public String archiverUtilisateur(@PathVariable int id) {
        utilisateurService.archiverUser(id);
        return "redirect:/lst-utilisateurs";
    }
    @PostMapping("/desarchiver/{id}")
    public String desarchiverUtilisateur(@PathVariable int id) {
        utilisateurService.desarchiverUser(id);
        return "redirect:/lst-utilisateurs";
    }
    @PostMapping("/activer/{id}")
    public String activerUtilisateur(@PathVariable int id) {
        utilisateurService.activerUser(id);
        return "redirect:/lst-utilisateurs";
    }
    @PostMapping("/desactiver/{id}")
    public String desactiverUtilisateur(@PathVariable int id) {
        utilisateurService.desactiverUser(id);
        return "redirect:/lst-utilisateurs";
    }
    @GetMapping("/lst-utilisateurs-archives")
    public String listeArchives(Model model) {
        model.addAttribute("utilisateurs", utilisateurService.getAllUsersArchives());
        return "utilisateur-archive-list";
    }
    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Principal principal,
            Model model) {

        String email = principal.getName();
        Utilisateur utilisateur = utilisateurService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        model.addAttribute("utilisateur", utilisateur);
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Erreur : Le nouveau mot de passe et la confirmation ne correspondent pas.");
            return "parametres";
        }
        boolean success = utilisateurService.updatePassword(email, currentPassword, newPassword, confirmPassword);

        if (success) {
            model.addAttribute("message", "Mot de passe changé avec succès !");
        } else {
            model.addAttribute("error", "Erreur : vérifiez votre mot de passe actuel.");
        }

        return "parametres";
    }



}
