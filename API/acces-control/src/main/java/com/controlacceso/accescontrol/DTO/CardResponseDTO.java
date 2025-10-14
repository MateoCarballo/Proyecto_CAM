package com.controlacceso.accescontrol.DTO;

import java.time.LocalDateTime;

public record CardResponseDTO(
        String uid,
        boolean authorized,
        String message,
        LocalDateTime timestamp
) { }