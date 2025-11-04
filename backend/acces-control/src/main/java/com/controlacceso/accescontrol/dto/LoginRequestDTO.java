package com.controlacceso.accescontrol.dto;

import lombok.Builder;

@Builder
public record LoginRequestDTO(
        String email,
        String password
) {
}
