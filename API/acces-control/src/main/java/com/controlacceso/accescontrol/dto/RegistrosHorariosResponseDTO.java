package com.controlacceso.accescontrol.dto;

import java.util.List;

public record RegistrosHorariosResponseDTO(
        String rol,                    // "admin" o "usuario"
        List<EmpleadoRegistrosDTO> empleados
) {}

