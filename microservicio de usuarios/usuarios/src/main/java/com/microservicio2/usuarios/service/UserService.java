package com.microservicio2.usuarios.service;


import com.microservicio2.usuarios.dto.UserResponseDto;
import com.microservicio2.usuarios.dto.UserUpdateDto;
import com.microservicio2.usuarios.model.Authority;
import com.microservicio2.usuarios.model.User;
import com.microservicio2.usuarios.repository.AuthorityRepository;
import com.microservicio2.usuarios.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponseDto> listarUsuarios() {
        return userRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDto> buscarPorUsername(String username) {
        return userRepository.findById(username)
                .map(this::convertirADTO);
    }


    public List<UserResponseDto> buscarPorTexto(String query, String rol) {
        return userRepository.buscarPorQueryYRol(query,rol).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public boolean existeUsuario(String username) {
        return userRepository.existsByUsername(username);
    }

    public User registrarUsuario(String username, String password,  String correo, String rol) {
        if (existeUsuario(username)) throw new RuntimeException("El usuario ya existe");

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setCorreo(correo);
        user.setEnabled(true);

        Authority authority = new Authority();
        authority.setAuthority(rol.startsWith("ROLE_") ? rol : "ROLE_" + rol);
        authority.setUser(user);
        user.setAuthorities(Collections.singleton(authority));

        return userRepository.save(user);
    }

    public Optional<UserResponseDto> actualizarUsuario(String username, UserUpdateDto dto) {
        return userRepository.findById(username).map(user -> {
            if (dto.getEnabled() != null) {
                user.setEnabled(dto.getEnabled());
            }
            if (dto.getRole() != null) {
                user.getAuthorities().clear();

                Authority authority = new Authority();
                authority.setAuthority(dto.getRole().startsWith("ROLE_") ? dto.getRole() : "ROLE_" + dto.getRole());
                authority.setUser(user);
                user.getAuthorities().add(authority);
            }

            return convertirADTO(userRepository.save(user));
        });
    }

    public void eliminarUsuario(String username) {
        userRepository.deleteById(username);
    }

    private UserResponseDto convertirADTO(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUsername(user.getUsername());
        dto.setCorreo(user.getCorreo());
        dto.setEnabled(user.isEnabled());
        List<String> roles = user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toList());
        dto.setRoles(roles);
        return dto;
    }
}
