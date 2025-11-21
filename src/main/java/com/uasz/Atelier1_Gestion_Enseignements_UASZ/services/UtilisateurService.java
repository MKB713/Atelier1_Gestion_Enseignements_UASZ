package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Utilisateur;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }

    @Transactional
    public Utilisateur saveUtilisateur(Utilisateur utilisateur) {
        validateUtilisateur(utilisateur);

        if (utilisateur.getId() == null) {
            // Nouvel utilisateur
            if (utilisateurRepository.existsByUsername(utilisateur.getUsername())) {
                throw new IllegalArgumentException("Le nom d'utilisateur existe déjà");
            }
            if (utilisateur.getEmail() != null && !utilisateur.getEmail().isEmpty() &&
                    utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
                throw new IllegalArgumentException("L'email existe déjà");
            }
            // Crypter le mot de passe
            utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        } else {
            // Modification
            Optional<Utilisateur> existing = utilisateurRepository.findByUsername(utilisateur.getUsername());
            if (existing.isPresent() && !existing.get().getId().equals(utilisateur.getId())) {
                throw new IllegalArgumentException("Le nom d'utilisateur existe déjà");
            }
            // Ne pas modifier le mot de passe s'il n'est pas changé
            if (utilisateur.getPassword() == null || utilisateur.getPassword().isEmpty()) {
                Utilisateur existingUser = utilisateurRepository.findById(utilisateur.getId())
                        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                utilisateur.setPassword(existingUser.getPassword());
            } else {
                // Vérifier si le mot de passe a changé
                Utilisateur existingUser = utilisateurRepository.findById(utilisateur.getId())
                        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                if (!utilisateur.getPassword().equals(existingUser.getPassword())) {
                    utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
                }
            }
        }

        return utilisateurRepository.save(utilisateur);
    }

    private void validateUtilisateur(Utilisateur utilisateur) {
        if (utilisateur.getUsername() == null || utilisateur.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur est obligatoire");
        }
        if (utilisateur.getId() == null && (utilisateur.getPassword() == null || utilisateur.getPassword().trim().isEmpty())) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire");
        }
        if (utilisateur.getNom() == null || utilisateur.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (utilisateur.getPrenom() == null || utilisateur.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }
        if (utilisateur.getRole() == null || utilisateur.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Le rôle est obligatoire");
        }
    }

    @Transactional
    public void deleteUtilisateur(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur introuvable");
        }
        utilisateurRepository.deleteById(id);
    }

    public Optional<Utilisateur> findByUsername(String username) {
        return utilisateurRepository.findByUsername(username);
    }
}