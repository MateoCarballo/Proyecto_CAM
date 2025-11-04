package com.controlacceso.accescontrol.dto;

import lombok.Builder;

@Builder
public record RegisterResponseDTO(
        boolean success,
        String mensaje
) {
}
