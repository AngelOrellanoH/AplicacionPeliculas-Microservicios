package com.microservicio_frontend.dto.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private Boolean enabled;
    private String role;
}
