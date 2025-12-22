package com.uasz.daos.auth.services;

import com.uasz.daos.auth.model.Enseignant;
import com.uasz.daos.auth.model.Utilisateur;
import com.uasz.daos.auth.enums.Role;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Utilisateur utilisateur;
    private final Enseignant enseignant;

    // Constructeur pour Utilisateur
    public CustomUserDetails(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.enseignant = null;
    }

    // Constructeur pour Enseignant
    public CustomUserDetails(Enseignant enseignant) {
        this.utilisateur = null;
        this.enseignant = enseignant;
    }

    // -------- Accès unifié aux données --------

    public String getNom() {
        if (utilisateur != null) {
            return utilisateur.getNom();
        } else if (enseignant != null) {
            return enseignant.getNom();
        }
        return "";
    }

    public String getPrenom() {
        if (utilisateur != null) {
            return utilisateur.getPrenom();
        } else if (enseignant != null) {
            return enseignant.getPrenom();
        }
        return "";
    }

    public Role getRole() {
        if (utilisateur != null) {
            return utilisateur.getRole();
        } else {
            // Pour les enseignants, on retourne le rôle ENSEIGNANT par défaut
            return Role.ENSEIGNANT;
        }
    }

    /** Retourne l’entité d'origine (Utilisateur OU Enseignant) */
    public Object getEntity() {
        return (utilisateur != null) ? utilisateur : enseignant;
    }

    // -------- Implémentation UserDetails --------

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = getRole();
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        if (utilisateur != null) {
            return utilisateur.getPassword();
        } else {
            // Pour les enseignants, pas de mot de passe dans ce système
            return "";
        }
    }

    @Override
    public String getUsername() {
        if (utilisateur != null) {
            return utilisateur.getEmail();
        } else if (enseignant != null) {
            return enseignant.getEmail();
        }
        return "";
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        if (utilisateur != null) return utilisateur.getEtat() != null;
        return enseignant != null && enseignant.isEstActif();
    }
}
