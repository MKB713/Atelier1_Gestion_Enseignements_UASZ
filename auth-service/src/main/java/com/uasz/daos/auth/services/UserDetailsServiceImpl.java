package com.uasz.daos.auth.services;

import com.uasz.daos.auth.model.Enseignant;
import com.uasz.daos.auth.model.Utilisateur;
import com.uasz.daos.auth.repository.EnseignantRepository;
import com.uasz.daos.auth.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final EnseignantRepository enseignantRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findByEmail(email);
        if (utilisateurOptional.isPresent()) {
            return new CustomUserDetails(utilisateurOptional.get());
        }

        Optional<Enseignant> enseignantOptional = enseignantRepository.findByEmail(email);
        if (enseignantOptional.isPresent()) {
            return new CustomUserDetails(enseignantOptional.get());
        }

        throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email);
    }
}
