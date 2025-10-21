package com.controlacceso.accescontrol.controller;

import com.controlacceso.accescontrol.DTO.LoginRequestDTO;
import com.controlacceso.accescontrol.DTO.LoginResponseDTO;
import com.controlacceso.accescontrol.DTO.RegisterRequestDTO;
import com.controlacceso.accescontrol.DTO.RegisterResponseDTO;
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
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO request){
        RegisterResponseDTO response = authService.register(request);
        if (!response.success()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
