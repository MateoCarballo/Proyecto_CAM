package com.controlacceso.accescontrol.service;

import com.controlacceso.accescontrol.DTO.LoginRequestDTO;
import com.controlacceso.accescontrol.DTO.LoginResponseDTO;
import com.controlacceso.accescontrol.DTO.RegisterRequestDTO;
import com.controlacceso.accescontrol.DTO.RegisterResponseDTO;
import com.controlacceso.accescontrol.entity.Empleado;
import com.controlacceso.accescontrol.entity.Rol;
import com.controlacceso.accescontrol.entity.UsuarioApp;
import com.controlacceso.accescontrol.repository.EmpleadoRepository;
import com.controlacceso.accescontrol.repository.RolRepository;
import com.controlacceso.accescontrol.repository.UsuarioAppRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

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

    public AuthService(UsuarioAppRepository usuarioAppRepository, EmpleadoRepository empleadoRepository, RolRepository rolRepository) {
        this.usuarioAppRepository = usuarioAppRepository;
        this.empleadoRepository = empleadoRepository;
        this.rolRepository = rolRepository;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        Optional<UsuarioApp> user = usuarioAppRepository.findByEmail(request.email());

        if (user.isEmpty()) {
            return LoginResponseDTO.builder()
                    .token(null)
                    .tipo(null)
                    .mensaje("Usuario no encontrado")
                    .build();
        }

        UsuarioApp usuarioApp = user.get();
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
        String token = Jwts.builder()
                .setSubject(usuarioApp.getEmail())                    // Identificador único (email)
                .claim("rol", usuarioApp.getRol().getNombreRol())     // Rol del usuario
                .setIssuedAt(new Date())                           // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Expiración
                .signWith(SignatureAlgorithm.HS512, jwtSecret)     // Firma con clave secreta
                .compact();

        return new LoginResponseDTO(token, "Bearer", "Login exitoso");

    }

    public RegisterResponseDTO register(RegisterRequestDTO request) {
        Optional<Empleado> empleadoOptional = empleadoRepository.findByEmail(request.email());
        if (empleadoOptional.isEmpty()) {
            return new RegisterResponseDTO(
                    false,
                    "No existe ningun empleado con este email"
            );
        }
        if (usuarioAppRepository.findByEmail(request.email()).isPresent()) {
            return new RegisterResponseDTO(
                    false,
                    "El email ya está registrado");
        }
        Empleado empleado = empleadoOptional.get();

        String hashedPassword = BCrypt.hashpw(
                request.password(),
                BCrypt.gensalt()
        );

        Optional<Rol> rolOpt = rolRepository.findByNombreRol("usuario");
        if (rolOpt.isEmpty()) {
            return new RegisterResponseDTO(
                    false,
                    "No existe el rol usuario");
        }

        // Crear nuevo usuario
        UsuarioApp nuevoUsuario = new UsuarioApp();
        nuevoUsuario.setEmail(request.email());
        nuevoUsuario.setHashContrasena(hashedPassword);
        nuevoUsuario.setEmpleado(empleado);  // Debes asegurarte de enviar el empleado o su ID
        nuevoUsuario.setRol(rolOpt.get());            // O asignar un rol por defecto si no lo envías

        usuarioAppRepository.save(nuevoUsuario);

        return new RegisterResponseDTO(true, "Usuario registrado correctamente");

    }
}
