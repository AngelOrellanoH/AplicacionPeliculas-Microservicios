package com.microservicio2.usuarios.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
@Setter
@Getter
public class User {
    @Id
    private String username;

    private String password;

    private boolean enabled;

    @Column(unique = true, nullable = false)
    private String correo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Critica> criticas = new HashSet<>();


    // Getters y Setters con Lombok

}
