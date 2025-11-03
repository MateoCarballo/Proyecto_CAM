package com.controlacceso.accescontrol.dto;

import java.util.List;

// DTO para cada empleado
public record EmpleadoRegistrosDTO(
        Integer idEmpleado,           // Añadimos ID para identificar al empleado
        String nombreEmpleado,
        String numeroTarjeta,
        List<RegistroHorarioDiaDTO> registrosPorDia) { }
