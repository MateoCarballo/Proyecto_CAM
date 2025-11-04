package com.controlacceso.accescontrol.dto;

import java.time.LocalDate;

public record RegistroFiltroDTO(
        String email,
        LocalDate fechaInicio,
        LocalDate fechaFin
) {}
