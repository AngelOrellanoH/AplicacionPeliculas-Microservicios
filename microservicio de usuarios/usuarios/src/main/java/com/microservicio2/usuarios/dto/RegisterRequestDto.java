package com.microservicio2.usuarios.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    private String username;
    private String password;
    private String role;
    private String correo;

    // Getter y Setter con Lombok
}
