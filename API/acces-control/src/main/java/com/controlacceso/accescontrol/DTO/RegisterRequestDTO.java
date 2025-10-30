package com.controlacceso.accescontrol.DTO;

import lombok.Builder;

@Builder
public record RegisterRequestDTO(
        Integer empleadoId,
        String email,
        String password
) {
}
