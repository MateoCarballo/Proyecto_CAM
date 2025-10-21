package com.controlacceso.accescontrol.DTO;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CardResponseDTO(
        String uid,
        boolean authorized,
        String message,
        LocalDateTime timestamp
) { }