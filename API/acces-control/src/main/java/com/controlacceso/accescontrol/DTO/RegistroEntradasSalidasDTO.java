package com.controlacceso.accescontrol.DTO;

import java.util.List;

public record RegistroEntradasSalidasDTO(
        String empleado,
        Integer idEmpleado,
        String numeroTarjeta,
        List<RegistrosEntradasSalidasDetalleDTO> registros
) {
}
