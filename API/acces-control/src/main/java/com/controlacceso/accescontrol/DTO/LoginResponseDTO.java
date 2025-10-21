package com.controlacceso.accescontrol.DTO;

import lombok.Builder;

@Builder
public record LoginResponseDTO(
        String token,
        String tipo,
        String mensaje
) {
}
