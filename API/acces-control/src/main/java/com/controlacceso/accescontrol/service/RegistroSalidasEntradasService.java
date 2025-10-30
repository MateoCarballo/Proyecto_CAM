package com.controlacceso.accescontrol.service;

import com.controlacceso.accescontrol.DTO.RegistroCompletoHorariosDTO;
import com.controlacceso.accescontrol.DTO.RegistroHorarioDiaDTO;
import com.controlacceso.accescontrol.DTO.TramoHorarioDTO;
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

    private final EmpleadoRepository empleadoRepository;
    private final TarjetaRepository tarjetaRepository;
    private final RegistroRepository registroRepository;
    private final UsuarioAppRepository usuarioAppRepository;

    public RegistroSalidasEntradasService(EmpleadoRepository empleadoRepository,
                                          TarjetaRepository tarjetaRepository,
                                          RegistroRepository registroRepository, UsuarioAppRepository usuarioAppRepository) {
        this.empleadoRepository = empleadoRepository;
        this.tarjetaRepository = tarjetaRepository;
        this.registroRepository = registroRepository;
        this.usuarioAppRepository = usuarioAppRepository;
    }

    public RegistroCompletoHorariosDTO obtenerRegistrosPorEmail(String email) {
        Optional<UsuarioApp> usuarioAppOptional = usuarioAppRepository.findByEmail(email);
        //Optional<Empleado> empleadoOpt = empleadoRepository.findByEmail(email);
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
/* CODIGO PREVIO A MODIFICAR LA BASE DE DATOS PARA SEPARA FECHA Y HORA

List<RegistrosEntradasSalidasDetalleDTO> registros = new ArrayList<>();
        // 1. Obtener el empleado
        Empleado empleado = empleadoRepository.findByEmail(email).get();
        // 2. Obtener la tarjeta asociada
        //TODO los casos de mas de una tarjeta pueden dar error
        List<Tarjeta> tarjeta = tarjetaRepository.findByEmpleadoId(empleado.getId());
        if (tarjeta.isEmpty()) {
            return RegistroEntradasSalidasDTO.builder()
                    .nombreEmpleado(empleado.getNombre().concat(" ").concat(empleado.getApellidos()))
                    .idEmpleado(empleado.getId())
                    .numeroTarjeta("-1")
                    .registrosHorariosPorDia(new HashMap<>())
                    .build();
        }
        // 3. Obtener todos los registros del empleado
        // 3.1 Obtener los valores de la tabla 'registros' en la base de datos
        List<Registro> registrosDB = registroRepository
                .findAllByEmpleadoIdOrderByFechaHoraDesc(empleado.getId());

        if (registrosDB.isEmpty()){
            return RegistroEntradasSalidasDTO.builder()
                    .nombreEmpleado(empleado.getNombre() + " " + empleado.getApellidos())
                    .idEmpleado(empleado.getId())
                    .numeroTarjeta(tarjeta.getFirst().getUid())
                    .registrosHorariosPorDia(new HashMap<>())
                    .build();
        }

        // 3.2 HashMap para poder guardar todas las entradas y salidas por dia.
        Map<LocalDate, List<Registro>> agrupadosPorFecha = new HashMap<>();

        for (Registro r : registrosDB) {
            LocalDate fecha = r.getFechaHora().toLocalDate();
            if (!agrupadosPorFecha.containsKey(fecha)) {
                agrupadosPorFecha.put(fecha, new ArrayList<>());
            }
            agrupadosPorFecha.get(fecha).add(r);
        }
        // 3.3 Entry para recorrer cada par clave-valor del hashmap
        for (Map.Entry<LocalDate, List<Registro>> entrada : agrupadosPorFecha.entrySet()) {
            LocalDate fecha = entrada.getKey();          // la fecha del día
            List<Registro> registrosDelDia = entrada.getValue();// todos los registros de ese día
            registrosDelDia.sort(Comparator.comparing(Registro::getFechaHora));

            String horaEntrada = null;
            List<TramoHorario> tramos = new ArrayList<>();
            // Recorremos el dia
            for (Registro registro : registrosDelDia) {
                //Guardamos la hora de la tupla
                String hora = registro.getFechaHora().toLocalTime().toString();

                if (registro.getTipo() == Registro.TipoRegistro.entrada) {
                    horaEntrada = hora;
                } else if (registro.getTipo() == Registro.TipoRegistro.salida && horaEntrada != null) {
                    tramos.add(new TramoHorario(horaEntrada, hora));
                    horaEntrada = null;
                }
            }

            if (horaEntrada != null) {
                tramos.add(new TramoHorario(horaEntrada, null));
            }
            //TODO revisar como controlar que alguien se olvide de fichar. Quizás desde la DB haciendo que si lel ultimo registro es una entrada meter una salida a las 23:59

            return RegistroEntradasSalidasDTO.builder()
                    .nombreEmpleado(empleado.getNombre().concat(" ").concat(empleado.getApellidos()))
                    .numeroTarjeta(empleado.getTarjetas().getFirst().getUid())
                    .idEmpleado(empleado.getId())
                    .registrosHorariosPorDia()
                    .build();
        }

        // 4. Agrupar los registros por fecha

        // 5. Construir la lista de DTO

        // 6. Devolver DTO final

 */