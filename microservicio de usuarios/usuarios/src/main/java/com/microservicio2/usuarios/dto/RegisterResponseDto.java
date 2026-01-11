package com.microservicio2.usuarios.dto;

import com.microservicio2.usuarios.model.Authority;
import com.microservicio2.usuarios.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class RegisterResponseDto {
    private String username;
    private String correo;
    private List<String> roles;

    public RegisterResponseDto(User user) {
        this.username = user.getUsername();
        this.correo = user.getCorreo();
        this.roles = user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toList());
    }
}
