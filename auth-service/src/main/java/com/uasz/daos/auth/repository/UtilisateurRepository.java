package com.uasz.daos.auth.repository;

import com.uasz.daos.auth.model.Utilisateur;
import com.uasz.daos.auth.enums.Etat;
import com.uasz.daos.auth.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByEmail(String email);
    List<Utilisateur> findByRoleNotAndEtatNot(Role role , Etat etat);
    List<Utilisateur> findByEtat(Etat etat);
    List<Utilisateur> findByRoleNotInAndEtatNot(List<Role> roles, Etat etat);


}
