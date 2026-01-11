package com.microservicio2.usuarios.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class UserResponseDto {
    private String username;
    private String correo;
    private boolean enabled;
    private List<String> roles;
}
