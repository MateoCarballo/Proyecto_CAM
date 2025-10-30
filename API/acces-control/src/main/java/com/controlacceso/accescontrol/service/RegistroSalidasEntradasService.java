package com.controlacceso.accescontrol.service;

import com.controlacceso.accescontrol.dto.RegistroCompletoHorariosDTO;
import com.controlacceso.accescontrol.dto.RegistroFiltroDTO;
import com.controlacceso.accescontrol.dto.RegistroHorarioDiaDTO;
import com.controlacceso.accescontrol.dto.TramoHorarioDTO;
import com.controlacceso.accescontrol.entity.Empleado;
import com.controlacceso.accescontrol.entity.Registro;
import com.controlacceso.accescontrol.entity.Tarjeta;
import com.controlacceso.accescontrol.entity.UsuarioApp;
import com.controlacceso.accescontrol.repository.EmpleadoRepository;
import com.controlacceso.accescontrol.repository.RegistroRepository;
import com.controlacceso.accescontrol.repository.TarjetaRepository;
import com.controlacceso.accescontrol.repository.UsuarioAppRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class RegistroSalidasEntradasService {

    private final TarjetaRepository tarjetaRepository;
    private final RegistroRepository registroRepository;
    private final UsuarioAppRepository usuarioAppRepository;

    public RegistroSalidasEntradasService(EmpleadoRepository empleadoRepository,
                                          TarjetaRepository tarjetaRepository,
                                          RegistroRepository registroRepository, UsuarioAppRepository usuarioAppRepository) {
        this.tarjetaRepository = tarjetaRepository;
        this.registroRepository = registroRepository;
        this.usuarioAppRepository = usuarioAppRepository;
    }

    public RegistroCompletoHorariosDTO obtenerRegistrosPorEmail(RegistroFiltroDTO filtro) {
        Optional<UsuarioApp> usuarioAppOptional = usuarioAppRepository.findByEmail(filtro.email());
        Empleado empleado;
        if (usuarioAppOptional.isEmpty()) {
            return RegistroCompletoHorariosDTO.builder()
                    .idEmpleado(-1)
                    .nombreEmpleado("No existe")
                    .numeroTarjeta("No existe")
                    .build();
        }
        empleado = usuarioAppOptional.get().getEmpleado();

        List<Tarjeta> tarjetas = tarjetaRepository.findByEmpleadoId(empleado.getId());
        String numeroTarjeta = tarjetas.isEmpty()
                ? "Sin tarjeta"
                : tarjetas.getFirst().getUid();

        List<Registro> registros = registroRepository
                .findAllByEmpleadoIdOrderByFechaDescHoraDesc(empleado.getId());

        /*
        * Mejora para poder filtrar por rangos de fechas y devolver entradas y salidas de esas fechas
        * List<Registro> registros = registroRepository
            .findByEmpleadoIdAndFechaBetweenOrderByFechaDescHoraDesc(
            empleado.getId(), filtro.fechaInicio(), filtro.fechaFin());

        * */

        /*
        Agrupa las tuplas de la tabla registros por fecha

        ComputeIfAbsent crea un arrrayList si no existe y si existe
         lo recoge para insertar un nuevo valor en la lista
         */
        Map<LocalDate, List<Registro>> registrosPorDia = new TreeMap<>();
        for (Registro r : registros) {
            registrosPorDia
                    .computeIfAbsent(r.getFecha(), k -> new ArrayList<>())
                    .add(r);
        }
        List<RegistroHorarioDiaDTO> registrosPorDiaDTO = new ArrayList<>();

        for (Map.Entry<LocalDate, List<Registro>> entry : registrosPorDia.entrySet()) {
            LocalDate fecha = entry.getKey();
            List<Registro> registrosDelDia = entry.getValue();

            // Ordenar cronológicamente por hora
            registrosDelDia.sort(Comparator.comparing(Registro::getHora));

            List<TramoHorarioDTO> tramos = getTramoHorarioDTOS(registrosDelDia);

            registrosPorDiaDTO.add(new RegistroHorarioDiaDTO(fecha.toString(), tramos));
        }

        RegistroCompletoHorariosDTO dtoFinal = RegistroCompletoHorariosDTO.builder()
                .nombreEmpleado(empleado.getNombre() + " " + empleado.getApellidos())
                .idEmpleado(empleado.getId())
                .numeroTarjeta(numeroTarjeta)
                .registrosHorariosPorDia(registrosPorDiaDTO)
                .build();

        return dtoFinal;

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