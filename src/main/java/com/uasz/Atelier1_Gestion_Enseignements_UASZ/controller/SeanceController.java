import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.SeanceDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.SeanceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SeanceController {

    @Autowired
    private SeanceService seanceService;

    @GetMapping("/seances")
    public List<Seance> getAllSeances() {
        return seanceService.getAllSeances();
    }

    @GetMapping("/seances/{id}")
    public Seance getSeanceById(@PathVariable Long id) {
        return seanceService.getSeanceById(id);
    }

    @PostMapping("/seances")
    public Seance createSeance(@RequestBody SeanceDTO seanceDTO) {
        return seanceService.createSeance(seanceDTO);
    }

    @PutMapping("/seances/{id}")
    public Seance updateSeance(@PathVariable Long id, @RequestBody SeanceDTO seanceDTO) {
        return seanceService.updateSeance(id, seanceDTO);
    }

    @DeleteMapping("/seances/{id}")
    public ResponseEntity<Void> deleteSeance(@PathVariable Long id) {
        try {
            seanceService.deleteSeance(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/planning/salle/{id}")
    public List<Seance> getPlanningBySalle(@PathVariable("id") Long salleId) {
        return seanceService.getBySalle(salleId);
    }

    @GetMapping("/seances/enseignant/{id}")
    public List<Seance> getSeancesByEnseignant(@PathVariable("id") Long enseignantId) {
        return seanceService.getByEnseignant(enseignantId);
    }
}
