package com.uasz.daos.deroulement.api;

import com.uasz.daos.deroulement.dto.SeanceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "emploi-temps-service")
public interface EmploiTempsApi {

    @GetMapping("/api/seances/{id}")
    SeanceDTO getSeanceById(@PathVariable("id") Long id);
}
