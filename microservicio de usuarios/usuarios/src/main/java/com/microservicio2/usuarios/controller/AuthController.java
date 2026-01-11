package com.microservicio2.usuarios.controller;


import com.microservicio2.usuarios.dto.*;
import com.microservicio2.usuarios.model.User;
import com.microservicio2.usuarios.security.JwtUtil;
import com.microservicio2.usuarios.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UserService userService) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto , HttpServletResponse response) {
        // Autenticar al usuario
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(), loginRequestDto.getPassword()
                )
        );

        // Obtener el rol
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        String token = jwtUtil.generateToken(loginRequestDto.getUsername(), role);

        // Crear cookie HttpOnly con el token
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(3600) // 1 hora
                .sameSite("Strict")
                .build();

        // Añadir la cookie a la respuesta
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok("Inicio de sesión exitoso");
    }

    // Registrar nuevo usuario con rol USER o ADMIN
    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegisterRequestDto request) {
        try {
            User user = userService.registrarUsuario(
                    request.getUsername(), request.getPassword(), request.getCorreo(), "ROLE_USER"
            );
            return ResponseEntity.ok(new RegisterResponseDto(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> obtenerDatosUsuario(Authentication authentication) {
        String username = authentication.getName();

        return userService.buscarPorUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
