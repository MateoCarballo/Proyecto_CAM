package com.controlacceso.accescontrol.dto;

import lombok.Builder;

@Builder
public record RegisterRequestDTO(
        Integer empleadoId,
        String email,
        String password
) {
}
