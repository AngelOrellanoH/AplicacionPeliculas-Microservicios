package com.microservicio_frontend.security;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    @ModelAttribute
    public void addUserAttributes(Model model, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        String role = (String) request.getAttribute("role");

        boolean isAuthenticated = username != null;

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("username", username);
        model.addAttribute("isAdmin", role != null && role.equals("ROLE_ADMIN"));
    }
}
