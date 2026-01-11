package com.microservicio1.peliculas.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class ActorResponseDto {

    private Integer id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String paisNacimiento;

}
