package com.controlacceso.accescontrol.dto;

import lombok.Builder;

@Builder
public record LoginResponseDTO(
        String token,
        String tipo,
        String mensaje
) {
}
