package com.controlacceso.accescontrol.dto;

import lombok.Builder;

import java.util.List;
@Builder
public record RegistroCompletoHorariosDTO(
        String nombreEmpleado,
        Integer idEmpleado,
        String numeroTarjeta,
        List<RegistroHorarioDiaDTO> registrosHorariosPorDia
) {
}
