package com.microservicio2.usuarios.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Criticas")
@Setter
@Getter
public class Critica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer idPelicula;

    private String valoracion;

    private Integer nota;

    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    // Getters y Setters con Lombok

}
