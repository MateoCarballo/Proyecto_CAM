package com.controlacceso.accescontrol.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CardResponseDTO(
        String uid,
        boolean authorized,
        String message,
        LocalDateTime timestamp
) { }