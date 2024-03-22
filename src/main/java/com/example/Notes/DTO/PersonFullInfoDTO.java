package com.example.Notes.DTO;


import com.example.Notes.models.enums.Role;
import lombok.Data;

import java.util.Set;


@Data
public class PersonFullInfoDTO {

    private String username;
    private String email;
    private Set<Role> roles;
}
