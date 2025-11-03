package com.controlacceso.accescontrol.service;

import com.controlacceso.accescontrol.config.security.JwtTokenProvider; // ✅ añadido
import com.controlacceso.accescontrol.dto.*;
import com.controlacceso.accescontrol.entity.*;
import com.controlacceso.accescontrol.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class RegistroSalidasEntradasService {

    private final TarjetaRepository tarjetaRepository;
    private final RegistroRepository registroRepository;
    private final UsuarioAppRepository usuarioAppRepository;
    private final JwtTokenProvider jwtTokenProvider; // ✅ nuevo campo

    public RegistroSalidasEntradasService(
            TarjetaRepository tarjetaRepository,
            RegistroRepository registroRepository,
            UsuarioAppRepository usuarioAppRepository,
            JwtTokenProvider jwtTokenProvider // ✅ lo inyectamos
    ) {
        this.tarjetaRepository = tarjetaRepository;
        this.registroRepository = registroRepository;
        this.usuarioAppRepository = usuarioAppRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public RegistroCompletoHorariosDTO obtenerRegistrosPorEmail(RegistroFiltroDTO filtro) {
        // ✅ Obtenemos email autenticado desde SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailAutenticado = auth.getName();

        // ✅ Recuperamos el usuario autenticado
        Optional<UsuarioApp> usuarioAppOptional = usuarioAppRepository.findByEmail(emailAutenticado);
        if (usuarioAppOptional.isEmpty()) {
            return RegistroCompletoHorariosDTO.builder()
                    .idEmpleado(-1)
                    .nombreEmpleado("No existe")
                    .numeroTarjeta("No existe")
                    .build();
        }

        UsuarioApp usuario = usuarioAppOptional.get();
        Empleado empleado = usuario.getEmpleado();

        // ✅ Si es ADMIN → ver todos los registros
        if (usuario.getRol().getNombreRol().equalsIgnoreCase("admin")) {
            return obtenerRegistrosDeTodosLosEmpleados();
        }

        // ✅ Si es USUARIO → ver solo los suyos
        return obtenerRegistrosDeEmpleado(empleado);
    }

    // ============================================
    // 🔹 Obtener registros de un empleado concreto
    // ============================================
    private RegistroCompletoHorariosDTO obtenerRegistrosDeEmpleado(Empleado empleado) {
        List<Tarjeta> tarjetas = tarjetaRepository.findByEmpleadoId(empleado.getId());
        String numeroTarjeta = tarjetas.isEmpty() ? "Sin tarjeta" : tarjetas.getFirst().getUid();

        List<Registro> registros = registroRepository.findAllByEmpleadoIdOrderByFechaDescHoraDesc(empleado.getId());

        Map<LocalDate, List<Registro>> registrosPorDia = agruparRegistrosPorDia(registros);

        List<RegistroHorarioDiaDTO> registrosPorDiaDTO = convertirARegistroHorarioDiaDTO(registrosPorDia);

        return RegistroCompletoHorariosDTO.builder()
                .nombreEmpleado(empleado.getNombre() + " " + empleado.getApellidos())
                .idEmpleado(empleado.getId())
                .numeroTarjeta(numeroTarjeta)
                .registrosHorariosPorDia(registrosPorDiaDTO)
                .build();
    }

    // ============================================
    // 🔹 Obtener registros de TODOS los empleados
    // ============================================
    private RegistroCompletoHorariosDTO obtenerRegistrosDeTodosLosEmpleados() {
        List<Registro> registros = registroRepository.findAllByOrderByFechaDescHoraDesc();

        Map<LocalDate, List<Registro>> registrosPorDia = agruparRegistrosPorDia(registros);
        List<RegistroHorarioDiaDTO> registrosPorDiaDTO = convertirARegistroHorarioDiaDTO(registrosPorDia);

        return RegistroCompletoHorariosDTO.builder()
                .nombreEmpleado("Administrador - Todos los empleados")
                .idEmpleado(0)
                .numeroTarjeta("N/A")
                .registrosHorariosPorDia(registrosPorDiaDTO)
                .build();
    }

    // ============================================
    // 🔹 Utilidades de agrupación y conversión
    // ============================================
    private Map<LocalDate, List<Registro>> agruparRegistrosPorDia(List<Registro> registros) {
        Map<LocalDate, List<Registro>> registrosPorDia = new TreeMap<>();
        for (Registro r : registros) {
            registrosPorDia.computeIfAbsent(r.getFecha(), k -> new ArrayList<>()).add(r);
        }
        return registrosPorDia;
    }

    private List<RegistroHorarioDiaDTO> convertirARegistroHorarioDiaDTO(Map<LocalDate, List<Registro>> registrosPorDia) {
        List<RegistroHorarioDiaDTO> registrosPorDiaDTO = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Registro>> entry : registrosPorDia.entrySet()) {
            LocalDate fecha = entry.getKey();
            List<Registro> registrosDelDia = entry.getValue();
            registrosDelDia.sort(Comparator.comparing(Registro::getHora));

            List<TramoHorarioDTO> tramos = getTramoHorarioDTOS(registrosDelDia);
            registrosPorDiaDTO.add(new RegistroHorarioDiaDTO(fecha.toString(), tramos));
        }
        return registrosPorDiaDTO;
    }

    private static List<TramoHorarioDTO> getTramoHorarioDTOS(List<Registro> registrosDelDia) {
        List<TramoHorarioDTO> tramos = new ArrayList<>();
        String horaEntrada = null;

        for (Registro reg : registrosDelDia) {
            String hora = reg.getHora().toString();

            if (reg.getTipo() == Registro.TipoRegistro.entrada) {
                horaEntrada = hora;
            } else if (reg.getTipo() == Registro.TipoRegistro.salida && horaEntrada != null) {
                tramos.add(new TramoHorarioDTO(horaEntrada, hora));
                horaEntrada = null;
            }
        }

        // Si el día termina con una entrada sin salida
        if (horaEntrada != null) {
            tramos.add(new TramoHorarioDTO(horaEntrada, null));
        }
        return tramos;
    }
}
