package com.uasz.daos.deroulement.api;

import com.uasz.daos.deroulement.dto.EnseignantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "enseignant-service")
public interface EnseignantApi {

    @GetMapping("/api/enseignants/{id}")
    EnseignantDTO getEnseignantById(@PathVariable("id") Long id);
}
