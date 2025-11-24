package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Utilisateur;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Etat;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Role;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailService  mailService;
    public Utilisateur createUser(Utilisateur utilisateur) {
        utilisateur.setEtat(Etat.ACTIF);
        String password = generatePassword();
        System.out.println("Password: " + password);
        utilisateur.setPassword(bCryptPasswordEncoder.encode(password));
        String subject = " Création de votre compte UASZ ";
        String text = "Bonjour " + utilisateur.getNom() + " " + utilisateur.getPrenom() + " !\n\n"
                + "Votre compte a été créé avec succès.\n"
                + "Voici vos identifiants de connexion :\n"
                + "Email : " + utilisateur.getEmail() + "\n"
                + "Mot de passe : " + password + "\n\n"
                + "Merci de conserver ces informations en lieu sûr.\n"
                + "Cordialement,\n"
                + "L'équipe UASZ";

        mailService.sendMail(utilisateur.getEmail(),subject,text);
        return utilisateurRepository.save(utilisateur);
    }
    public List<Utilisateur> findAll(Utilisateur currentUser) {
        if (currentUser == null) {
            return List.of();
        }
        Role role = currentUser.getRole();
        if (role.equals(Role.ADMIN)) {
            return utilisateurRepository.findByRoleNotAndEtatNot(Role.ADMIN, Etat.ARCHIVE);
        } else if (role.equals(Role.CHEF_DE_DEPARTEMENT)) {
            return utilisateurRepository.findByRoleNotInAndEtatNot(
                    List.of(Role.ADMIN, Role.CHEF_DE_DEPARTEMENT), Etat.ARCHIVE);
        } else {

            return List.of();
        }
    }

    public Optional<Utilisateur> findUserByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }
    public Utilisateur updateUser(Utilisateur utilisateur) {
        Optional<Utilisateur> userOpt = findUserByEmail(utilisateur.getEmail());
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            if (utilisateur.getPassword() != null && !utilisateur.getPassword().isEmpty()) {
                user.setPassword(bCryptPasswordEncoder.encode(utilisateur.getPassword()));
            }

            user.setEmail(utilisateur.getEmail());
            user.setRole(utilisateur.getRole());
            user.setNom(utilisateur.getNom());
            user.setPrenom(utilisateur.getPrenom());
            user.setTelephone(utilisateur.getTelephone());

            return utilisateurRepository.save(user);
        } else {
            return null;
        }
    }

    public String generatePassword() {
        String password = UUID.randomUUID().toString().substring(0, 6);
        return password;
    }
    public Utilisateur activerUser(int id) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findById(id);
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            user.setEtat(Etat.ACTIF);
            return utilisateurRepository.save(user);
        }
        else {
            return null;
        }
    }
    public Utilisateur desactiverUser(int id) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findById(id);
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            user.setEtat(Etat.INACTIF);
            return utilisateurRepository.save(user);
        }
        else {
            return null;
        }
    }
    public Utilisateur archiverUser(int id) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findById(id);
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            user.setEtat(Etat.ARCHIVE);
            return utilisateurRepository.save(user);
        }
        else {
            return null;
        }
    }
    public Utilisateur desarchiverUser(int id) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findById(id);
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            user.setEtat(Etat.INACTIF);
            return utilisateurRepository.save(user);
        }
        else {
            return null;
        }
    }
    public List<Utilisateur> getAllUsersArchives(){
        return utilisateurRepository.findByEtat(Etat.ARCHIVE);
    }
    public boolean updatePassword(String email, String currentPassword, String newPassword, String confirmPassword) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return false;
        }
        Utilisateur user = userOpt.get();

        if (!bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }
        if (!newPassword.equals(confirmPassword)) {
            return false;
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        utilisateurRepository.save(user);

        return true;
    }
}
