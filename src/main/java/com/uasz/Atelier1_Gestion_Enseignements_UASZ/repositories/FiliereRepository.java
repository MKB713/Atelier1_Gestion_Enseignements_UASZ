package com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.*;
import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
public interface FiliereRepository extends JpaRepository<Filiere, Long> {

}