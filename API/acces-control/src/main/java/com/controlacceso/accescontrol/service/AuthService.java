package com.controlacceso.accescontrol.service;

import com.controlacceso.accescontrol.dto.LoginRequestDTO;
import com.controlacceso.accescontrol.dto.LoginResponseDTO;
import com.controlacceso.accescontrol.dto.RegisterRequestDTO;
import com.controlacceso.accescontrol.dto.RegisterResponseDTO;
import com.controlacceso.accescontrol.entity.Empleado;
import com.controlacceso.accescontrol.entity.Rol;
import com.controlacceso.accescontrol.entity.UsuarioApp;
import com.controlacceso.accescontrol.repository.EmpleadoRepository;
import com.controlacceso.accescontrol.repository.RolRepository;
import com.controlacceso.accescontrol.repository.UsuarioAppRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioAppRepository usuarioAppRepository;
    private final EmpleadoRepository empleadoRepository;
    private final RolRepository rolRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    public AuthService(UsuarioAppRepository usuarioAppRepository,
                       EmpleadoRepository empleadoRepository,
                       RolRepository rolRepository) {
        this.usuarioAppRepository = usuarioAppRepository;
        this.empleadoRepository = empleadoRepository;
        this.rolRepository = rolRepository;
    }

    // ========================================
    // ============= LOGIN ====================
    // ========================================
    public LoginResponseDTO login(LoginRequestDTO request) {
        Optional<UsuarioApp> userOpt = usuarioAppRepository.findByEmail(request.email());

        if (userOpt.isEmpty()) {
            return LoginResponseDTO.builder()
                    .token(null)
                    .tipo(null)
                    .mensaje("Usuario no encontrado")
                    .build();
        }

        UsuarioApp usuarioApp = userOpt.get();

        if (!usuarioApp.getEmpleado().isActivo()) {
            return LoginResponseDTO.builder()
                    .token(null)
                    .tipo(null)
                    .mensaje("Usuario inactivo")
                    .build();
        }

        if (!BCrypt.checkpw(request.password(), usuarioApp.getHashContrasena())) {
            return LoginResponseDTO.builder()
                    .token(null)
                    .tipo(null)
                    .mensaje("Contraseña incorrecta")
                    .build();
        }

        // Generar clave secreta y token JWT (versión 0.11.5)
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
                .setSubject(usuarioApp.getEmail())
                .claim("rol", usuarioApp.getRol().getNombreRol())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key) // no se pasa el algoritmo, se deduce de la clave
                .compact();

        return LoginResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .mensaje("Login exitoso")
                .build();
    }

    // ========================================
    // ============ REGISTER ==================
    // ========================================
    public RegisterResponseDTO register(RegisterRequestDTO request) {

        // 1. Buscar empleado por ID
        Optional<Empleado> empleadoOpt = empleadoRepository.findById(request.empleadoId());
        if (empleadoOpt.isEmpty()) {
            return RegisterResponseDTO.builder()
                    .success(false)
                    .mensaje("Empleado no encontrado")
                    .build();
        }

        Empleado empleado = empleadoOpt.get();

        // 2. Verificar que el empleado no tenga usuario
        if (empleado.getUsuarioApp() != null) {
            return RegisterResponseDTO.builder()
                    .success(false)
                    .mensaje("El empleado ya tiene un usuario registrado")
                    .build();
        }

        // 3. Verificar que el email no esté registrado
        if (usuarioAppRepository.findByEmail(request.email()).isPresent()) {
            return RegisterResponseDTO.builder()
                    .success(false)
                    .mensaje("El email ya está registrado")
                    .build();
        }

        // 4. Crear usuario con builder
        String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());

        Optional<Rol> rolOpt = rolRepository.findByNombreRol("usuario");
        if (rolOpt.isEmpty()) {
            return RegisterResponseDTO.builder()
                    .success(false)
                    .mensaje("No existe el rol 'usuario'")
                    .build();
        }

        UsuarioApp nuevoUsuario = UsuarioApp.builder()
                .empleado(empleado)
                .email(request.email())
                .hashContrasena(hashedPassword)
                .rol(rolOpt.get())
                .fechaCreacion(LocalDateTime.now())
                .build();

        usuarioAppRepository.save(nuevoUsuario);

        return RegisterResponseDTO.builder()
                .success(true)
                .mensaje("Usuario registrado correctamente")
                .build();
    }
}
