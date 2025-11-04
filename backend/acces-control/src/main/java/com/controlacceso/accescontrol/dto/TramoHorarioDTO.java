package com.controlacceso.accescontrol.dto;

import lombok.Builder;

@Builder
public record TramoHorarioDTO(
     String horaEntrada,
     String horaSalida
) {}
