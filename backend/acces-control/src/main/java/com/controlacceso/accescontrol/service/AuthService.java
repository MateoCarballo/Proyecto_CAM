package com.controlacceso.accescontrol.service;

import com.controlacceso.accescontrol.config.security.JwtTokenProvider; // ✅ IMPORTANTE: añadimos el uso centralizado del provider
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
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioAppRepository usuarioAppRepository;
    private final EmpleadoRepository empleadoRepository;
    private final RolRepository rolRepository;
    private final JwtTokenProvider jwtTokenProvider; // ✅ inyección del provider centralizado

    public AuthService(UsuarioAppRepository usuarioAppRepository,
                       EmpleadoRepository empleadoRepository,
                       RolRepository rolRepository,
                       JwtTokenProvider jwtTokenProvider) { // ✅ añadido en el constructor
        this.usuarioAppRepository = usuarioAppRepository;
        this.empleadoRepository = empleadoRepository;
        this.rolRepository = rolRepository;
        this.jwtTokenProvider = jwtTokenProvider;
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

        // ✅ Ahora usamos JwtTokenProvider para generar el token correctamente
        String token = jwtTokenProvider.generarToken(
                usuarioApp.getEmail(),
                usuarioApp.getRol().getNombreRol(),
                Long.valueOf(usuarioApp.getId())  // Al usar un long en la
        );

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

        // 4. Crear usuario
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
