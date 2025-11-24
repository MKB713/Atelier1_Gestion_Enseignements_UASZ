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

@RequiredArgsConstructor
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
        this.enseignant = enseignant;
        this.utilisateur = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role;
        if (utilisateur != null) {
            role = utilisateur.getRole();
        } else {
            role = enseignant.getRole();
        }
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

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        if (utilisateur != null) return utilisateur.getEtat() != null;
        if (enseignant != null) return enseignant.isEstActif();
        return false;
    }
}
