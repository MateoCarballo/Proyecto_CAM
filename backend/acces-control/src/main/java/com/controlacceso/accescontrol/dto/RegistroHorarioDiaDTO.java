package com.controlacceso.accescontrol.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RegistroHorarioDiaDTO(
        String fecha,
        List<TramoHorarioDTO> horarios
) {

}
