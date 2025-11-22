package com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String email;
    private String password;
    private Role role;
}
