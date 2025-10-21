package com.controlacceso.accescontrol.DTO;

import java.util.List;

public record RegistroEntradasSalidasDTO(
        String empleado,
        Long idEmpleado,
        String numeroTarjeta,
        List<RegistrosEntradasSalidasDetalleDTO> registros
) {
}
