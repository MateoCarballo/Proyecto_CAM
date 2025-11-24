package com.controlacceso.accescontrol.controller;

import com.controlacceso.accescontrol.dto.LoginRequestDTO;
import com.controlacceso.accescontrol.dto.LoginResponseDTO;
import com.controlacceso.accescontrol.dto.RegisterRequestDTO;
import com.controlacceso.accescontrol.dto.RegisterResponseDTO;
import com.controlacceso.accescontrol.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        //Una vez construida la respuesta desde nuestro service, aquí definimos en código de la
        //respuesta en función del mensaje generado. El caso 'default' es para otros errores no contemplados
        if (response.token() == null) {

            return switch (response.mensaje()) {
                case "Contraseña incorrecta" -> ResponseEntity.status(401).body(response);

                case "Usuario no encontrado" -> ResponseEntity.status(404).body(response);

                case "Usuario inactivo" -> ResponseEntity.status(403).body(response);

                default -> ResponseEntity.status(400).body(
                        new LoginResponseDTO(null, null, "Error desconocido")
                );
            };
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        RegisterResponseDTO response = authService.register(request);
        if (!response.success()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
