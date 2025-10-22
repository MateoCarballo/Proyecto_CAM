package com.controlacceso.accescontrol.DTO;

import lombok.Builder;

@Builder
public record TramoHorarioDTO(
     String horaEntrada,
     String horaSalida
) {}
