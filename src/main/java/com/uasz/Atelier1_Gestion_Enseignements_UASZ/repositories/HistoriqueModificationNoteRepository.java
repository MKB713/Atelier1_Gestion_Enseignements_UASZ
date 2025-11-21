package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.HistoriqueModificationNote;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.NoteCahierTexte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoriqueModificationNoteRepository extends JpaRepository<HistoriqueModificationNote, Long> {

    List<HistoriqueModificationNote> findByNoteOrderByDateModificationDesc(NoteCahierTexte note);

    List<HistoriqueModificationNote> findByNoteIdOrderByDateModificationDesc(Long noteId);

    List<HistoriqueModificationNote> findByTypeModification(String typeModification);

    List<HistoriqueModificationNote> findByDateModificationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    List<HistoriqueModificationNote> findByEnseignantModificateurId(Long enseignantId);
}
