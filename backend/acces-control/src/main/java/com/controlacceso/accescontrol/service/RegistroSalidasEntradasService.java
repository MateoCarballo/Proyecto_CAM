package com.controlacceso.accescontrol.service;

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

    public RegistroSalidasEntradasService(
            TarjetaRepository tarjetaRepository,
            RegistroRepository registroRepository,
            UsuarioAppRepository usuarioAppRepository
    ) {
        this.tarjetaRepository = tarjetaRepository;
        this.registroRepository = registroRepository;
        this.usuarioAppRepository = usuarioAppRepository;
    }

    public RegistrosHorariosResponseDTO obtenerRegistrosPorFiltro() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailAutenticado = auth.getName();

        Optional<UsuarioApp> usuarioOpt = usuarioAppRepository.findByEmail(emailAutenticado);
        if (usuarioOpt.isEmpty()) {
            return new RegistrosHorariosResponseDTO(
                    "desconocido",
                    Collections.emptyList()
            );
        }

        UsuarioApp usuario = usuarioOpt.get();
        String rol = usuario.getRol().getNombreRol();

        List<EmpleadoRegistrosDTO> empleadosDTO;
        if (rol.equalsIgnoreCase("admin")) {
            empleadosDTO = obtenerRegistrosDeTodosLosEmpleados();
        } else {
            empleadosDTO = Collections.singletonList(obtenerRegistrosDeEmpleado(usuario.getEmpleado()));
        }

        return new RegistrosHorariosResponseDTO(rol, empleadosDTO);
    }

    // -------------------
    // Registros de un empleado
    // -------------------
    private EmpleadoRegistrosDTO obtenerRegistrosDeEmpleado(Empleado empleado) {
        List<Tarjeta> tarjetas = tarjetaRepository.findByEmpleadoId(empleado.getId());
        String numeroTarjeta = tarjetas.isEmpty() ? "Sin tarjeta" : tarjetas.get(0).getUid();

        List<Registro> registros = registroRepository.findAllByEmpleadoIdOrderByFechaDescHoraDesc(empleado.getId());

        Map<LocalDate, List<Registro>> registrosPorDia = agruparRegistrosPorDia(registros);
        List<RegistroHorarioDiaDTO> registrosPorDiaDTO = convertirARegistroHorarioDiaDTO(registrosPorDia);

        return new EmpleadoRegistrosDTO(
                empleado.getId(),
                empleado.getNombre() + " " + empleado.getApellidos(),
                numeroTarjeta,
                registrosPorDiaDTO
        );
    }

    // -------------------
    // Registros de todos los empleados (admin)
    // -------------------
    private List<EmpleadoRegistrosDTO> obtenerRegistrosDeTodosLosEmpleados() {
        List<UsuarioApp> usuarios = usuarioAppRepository.findAll();
        List<EmpleadoRegistrosDTO> empleadosDTO = new ArrayList<>();

        for (UsuarioApp usuario : usuarios) {
            empleadosDTO.add(obtenerRegistrosDeEmpleado(usuario.getEmpleado()));
        }

        // Ordenar por idEmpleado
        empleadosDTO.sort(Comparator.comparingInt(EmpleadoRegistrosDTO::idEmpleado));
        return empleadosDTO;
    }

    // -------------------
    // Utilidades
    // -------------------
    private Map<LocalDate, List<Registro>> agruparRegistrosPorDia(List<Registro> registros) {
        Map<LocalDate, List<Registro>> registrosPorDia = new TreeMap<>();
        for (Registro r : registros) {
            registrosPorDia.computeIfAbsent(r.getFecha(), k -> new ArrayList<>()).add(r);
        }
        return registrosPorDia;
    }

    private List<RegistroHorarioDiaDTO> convertirARegistroHorarioDiaDTO(Map<LocalDate, List<Registro>> registrosPorDia) {
        List<RegistroHorarioDiaDTO> listaDTO = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Registro>> entry : registrosPorDia.entrySet()) {
            LocalDate fecha = entry.getKey();
            List<Registro> registrosDelDia = entry.getValue();
            registrosDelDia.sort(Comparator.comparing(Registro::getHora));

            List<TramoHorarioDTO> tramos = convertirATramos(registrosDelDia);
            listaDTO.add(new RegistroHorarioDiaDTO(fecha.toString(), tramos));
        }
        return listaDTO;
    }

    private List<TramoHorarioDTO> convertirATramos(List<Registro> registrosDelDia) {
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

        if (horaEntrada != null) {
            tramos.add(new TramoHorarioDTO(horaEntrada, null));
        }

        return tramos;
    }
}
