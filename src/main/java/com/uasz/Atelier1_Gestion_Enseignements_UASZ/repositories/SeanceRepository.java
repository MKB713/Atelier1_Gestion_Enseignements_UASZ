package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Classe;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Module;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {

    List<Seance> findByClasse(Classe classe);

    List<Seance> findByEnseignant(Enseignant enseignant);

    List<Seance> findByModule(Module module);

    List<Seance> findByDateSeance(LocalDate dateSeance);

    List<Seance> findByDateSeanceBetween(LocalDate dateDebut, LocalDate dateFin);

    List<Seance> findByClasseAndDateSeance(Classe classe, LocalDate dateSeance);

    List<Seance> findByEnseignantAndDateSeanceBetween(Enseignant enseignant, LocalDate dateDebut, LocalDate dateFin);

    List<Seance> findByEstTerminee(boolean estTerminee);

    List<Seance> findByTypeSeance(String typeSeance);
}
