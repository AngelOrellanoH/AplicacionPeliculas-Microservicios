package com.microservicio_frontend.dto.Actor;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ActorDto {
    private Integer id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String paisNacimiento;
}
