package com.controlacceso.accescontrol.DTO;

import lombok.Builder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
@Builder
public record RegistroCompletoHorariosDTO(
        String nombreEmpleado,
        Integer idEmpleado,
        String numeroTarjeta,
        List<RegistroHorarioDiaDTO> registrosHorariosPorDia
) {
}
