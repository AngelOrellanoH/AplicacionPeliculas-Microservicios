package com.microservicio_frontend.service;

import com.microservicio_frontend.dto.Auth.LoginRequestDto;
import com.microservicio_frontend.dto.Auth.RegistroRequestDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.util.Map;

@Service
public class AuthService {
    private final RestTemplate restTemplate;

    public AuthService(RestTemplate  restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Void> autenticarUsuario(LoginRequestDto credenciales) {
        String url = "http://localhost:8090/auth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequestDto> request = new HttpEntity<>(credenciales, headers);

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Void.class
        );
    }

    public void registrarUsuario(RegistroRequestDto dto) {
        String url = "http://localhost:8090/auth/registro";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Se puede omitir el campo passwordConfirm si el microservicio no lo necesita
        Map<String, String> body = Map.of(
                "correo", dto.getCorreo(),
                "username", dto.getUsername(),
                "password", dto.getPassword()
        );

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, request, Void.class);
    }
}
