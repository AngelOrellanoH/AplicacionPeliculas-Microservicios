package com.microservicio_frontend.dto.Auth;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroRequestDto {
    private String correo;
    private String username;
    private String password;
    private String passwordConfirm;
}
