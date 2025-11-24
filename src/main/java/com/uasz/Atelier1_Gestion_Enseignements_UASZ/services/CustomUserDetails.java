package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Utilisateur;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
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
        return (utilisateur != null) ? utilisateur.getNom() : enseignant.getNom();
    }

    public String getPrenom() {
        return (utilisateur != null) ? utilisateur.getPrenom() : enseignant.getPrenom();
    }

    public Role getRole() {
        return (utilisateur != null) ? utilisateur.getRole() : enseignant.getRole();
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
        return (utilisateur != null) ? utilisateur.getPassword() : enseignant.getPassword();
    }

    @Override
    public String getUsername() {
        return (utilisateur != null) ? utilisateur.getEmail() : enseignant.getEmail();
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
