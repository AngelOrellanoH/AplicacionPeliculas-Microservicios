package com.microservicio_frontend.controller;


import com.microservicio_frontend.dto.Auth.LoginRequestDto;
import com.microservicio_frontend.dto.Auth.RegistroRequestDto;
import com.microservicio_frontend.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin(Model model) {
        model.addAttribute("credenciales", new LoginRequestDto());
        return "auth/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute LoginRequestDto credenciales,
                                HttpServletResponse response,
                                Model model) {
        try {
            ResponseEntity<Void> loginResponse = authService.autenticarUsuario(credenciales);

            // Copiar cookies devueltas por el microservicio
            List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
            if (cookies != null) {
                for (String cookie : cookies) {
                    response.addHeader(HttpHeaders.SET_COOKIE, cookie);
                }
            }

            return "redirect:/";

        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().value() == 403) {
                model.addAttribute("mensajeError", "Credenciales incorrectas");
            } else {
                model.addAttribute("mensajeError", "Error de autenticación: " + ex.getStatusCode());
            }
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error de conexión con el servidor de autenticación");
        }

        model.addAttribute("credenciales", credenciales);
        return "auth/login";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("registro", new RegistroRequestDto());
        return "auth/registro";
    }
    
    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute RegistroRequestDto registro,
                                   Model model) {
        model.addAttribute("registro", registro);

        // Validación básica en el controlador
        if (!registro.getPassword().equals(registro.getPasswordConfirm())) {
            model.addAttribute("mensajeError", "Las contraseñas no coinciden");
            return "auth/registro";
        }

        try {
            authService.registrarUsuario(registro);
            model.addAttribute("mensajeExito", "Se registró correctamente. Redirigiendo al login...");

            // Redirige automáticamente con JS después de 2 segundos
            model.addAttribute("redirigir", true);
            return "auth/registro";

        } catch (HttpClientErrorException.BadRequest ex) {
            model.addAttribute("mensajeError", "Datos inválidos: " + ex.getResponseBodyAsString());
        } catch (Exception e) {
            model.addAttribute("mensajeError", "No se pudo completar el registro");
        }

        return "auth/registro";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return "redirect:/";
    }
}
