package com.controlacceso.accescontrol.service;

import com.controlacceso.accescontrol.DTO.RegistroEntradasSalidasDTO;
import com.controlacceso.accescontrol.DTO.RegistrosEntradasSalidasDetalleDTO;
import com.controlacceso.accescontrol.entity.Empleado;
import com.controlacceso.accescontrol.entity.Registro;
import com.controlacceso.accescontrol.entity.Tarjeta;
import com.controlacceso.accescontrol.repository.EmpleadoRepository;
import com.controlacceso.accescontrol.repository.RegistroRepository;
import com.controlacceso.accescontrol.repository.TarjetaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class RegistroService {

    private final EmpleadoRepository empleadoRepository;
    private final TarjetaRepository tarjetaRepository;
    private final RegistroRepository registroRepository;

    public RegistroService(EmpleadoRepository empleadoRepository,
                           TarjetaRepository tarjetaRepository,
                           RegistroRepository registroRepository) {
        this.empleadoRepository = empleadoRepository;
        this.tarjetaRepository = tarjetaRepository;
        this.registroRepository = registroRepository;
    }

    public RegistroEntradasSalidasDTO obtenerRegistrosPorEmail(String email) {

        // 1. Obtener el empleado


        // 2. Obtener la tarjeta asociada

        // 3. Obtener todos los registros del empleado

        // 4. Agrupar los registros por fecha

        // 5. Construir la lista de DTO

        // 6. Devolver DTO final
    }
}
