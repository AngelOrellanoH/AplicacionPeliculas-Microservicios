package com.microservicio_frontend.dto.User;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private String username;
    private String correo;
    private boolean enabled;
    private List<String> roles;
}
