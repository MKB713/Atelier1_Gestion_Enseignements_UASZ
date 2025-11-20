package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Ec;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.EcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EcService {

    @Autowired
    private EcRepository ecRepository;

    public List<Ec> getAllEcs() {
        return ecRepository.findAll();
    }

    public Ec getEcById(Long id) {
        return ecRepository.findById(id).orElse(null);
    }

    public Ec createEc(Ec ec) {
        return ecRepository.save(ec);
    }

    public Ec updateEc(Long id, Ec ecDetails) {
        Ec existingEc = getEcById(id);
        if (existingEc != null) {
            existingEc.setLibelle(ecDetails.getLibelle());
            existingEc.setCode(ecDetails.getCode());
            existingEc.setUe(ecDetails.getUe()); // Assuming Ue is also managed
            return ecRepository.save(existingEc);
        }
        return null;
    }

    public void deleteEc(Long id) {
        ecRepository.deleteById(id);
    }
}
