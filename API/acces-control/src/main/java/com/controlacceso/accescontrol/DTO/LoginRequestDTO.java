package com.controlacceso.accescontrol.DTO;

import lombok.Builder;

@Builder
public record LoginRequestDTO(
        String email,
        String password
) {
}
