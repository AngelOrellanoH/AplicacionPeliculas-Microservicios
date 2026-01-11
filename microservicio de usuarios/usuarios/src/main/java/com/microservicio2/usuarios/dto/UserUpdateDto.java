package com.microservicio2.usuarios.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {

    private Boolean enabled;
    private String role;
}
