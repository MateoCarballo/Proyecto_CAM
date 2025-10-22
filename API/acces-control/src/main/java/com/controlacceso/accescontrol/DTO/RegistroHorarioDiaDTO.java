package com.controlacceso.accescontrol.DTO;

import lombok.Builder;

import java.util.List;

@Builder
public record RegistroHorarioDiaDTO(
        String fecha,
        List<TramoHorarioDTO> horarios
) {

}
