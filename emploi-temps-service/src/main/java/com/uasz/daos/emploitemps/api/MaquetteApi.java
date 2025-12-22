package com.uasz.daos.emploitemps.api;

import com.uasz.daos.emploitemps.dto.ECDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "maquette-service")
public interface MaquetteApi {

    @GetMapping("/api/ecs/{id}")
    ECDTO getECById(@PathVariable("id") Long id);
}
