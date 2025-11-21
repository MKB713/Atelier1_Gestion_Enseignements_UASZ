package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.EC;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.ECRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ECService {

    @Autowired
    private ECRepository ecRepository;

    @Autowired
    private UEService ueService;

    public List<EC> getAllECs() {
        return ecRepository.findAll();
    }

    public Optional<EC> getECById(Long id) {
        return ecRepository.findById(id);
    }

    public List<EC> getECsByUE(Long ueId) {
        return ecRepository.findByUeId(ueId);
    }

    public List<EC> searchByLibelle(String libelle) {
        return ecRepository.findByLibelleContainingIgnoreCase(libelle);
    }

    @Transactional
    public EC saveEC(EC ec) {
        validateEC(ec);

        if (ec.getId() == null) {
            if (ecRepository.existsByCode(ec.getCode())) {
                throw new IllegalArgumentException("Un EC avec le code " + ec.getCode() + " existe déjà");
            }
        } else {
            Optional<EC> existingEC = ecRepository.findByCode(ec.getCode());
            if (existingEC.isPresent() && !existingEC.get().getId().equals(ec.getId())) {
                throw new IllegalArgumentException("Un autre EC utilise déjà le code " + ec.getCode());
            }
        }

        // Vérifier que l'UE existe
        if (ec.getUe() != null && ec.getUe().getId() != null) {
            ueService.getUEById(ec.getUe().getId())
                    .orElseThrow(() -> new IllegalArgumentException("UE non trouvée"));
        }

        return ecRepository.save(ec);
    }

    private void validateEC(EC ec) {
        if (ec.getCode() == null || ec.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Le code de l'EC est obligatoire");
        }
        if (ec.getLibelle() == null || ec.getLibelle().trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé de l'EC est obligatoire");
        }
        if (ec.getUe() == null) {
            throw new IllegalArgumentException("L'UE est obligatoire");
        }
        if (ec.getCredits() < 0) {
            throw new IllegalArgumentException("Le nombre de crédits ne peut pas être négatif");
        }
    }

    @Transactional
    public void deleteEC(Long id) {
        if (!ecRepository.existsById(id)) {
            throw new RuntimeException("EC introuvable avec l'ID: " + id);
        }
        ecRepository.deleteById(id);
    }
}