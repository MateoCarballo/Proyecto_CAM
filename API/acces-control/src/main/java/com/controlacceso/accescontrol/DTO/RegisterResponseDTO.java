package com.controlacceso.accescontrol.DTO;

import lombok.Builder;

@Builder
public record RegisterResponseDTO(
        boolean success,
        String mensaje
) {
}
