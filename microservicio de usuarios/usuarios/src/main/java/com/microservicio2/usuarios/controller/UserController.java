package com.microservicio2.usuarios.controller;


import com.microservicio2.usuarios.dto.UserResponseDto;
import com.microservicio2.usuarios.dto.UserUpdateDto;
import com.microservicio2.usuarios.model.User;
import com.microservicio2.usuarios.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDto> listarTodos() {
        return userService.listarUsuarios();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> obtenerUsuario(@PathVariable String username) {
        return userService.buscarPorUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public List<UserResponseDto> buscarPorTexto(@RequestParam(required = false) String query,
                                                @RequestParam(required = false) String rol ) {

        String filtroTexto = (query != null && !query.trim().isEmpty()) ? query.trim() : null;
        String filtroRol = (rol != null && !rol.trim().isEmpty()) ? rol.trim() : null;
        return userService.buscarPorTexto(filtroTexto, filtroRol);
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserResponseDto> actualizarUsuario(
            @PathVariable String username,
            @RequestBody UserUpdateDto dto) {
        return userService.actualizarUsuario(username, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> eliminar(@PathVariable String username) {
        userService.eliminarUsuario(username);
        return ResponseEntity.noContent().build();
    }
}
