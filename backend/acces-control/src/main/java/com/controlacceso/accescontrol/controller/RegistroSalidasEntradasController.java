package com.controlacceso.accescontrol.controller;

import com.controlacceso.accescontrol.dto.RegistrosHorariosResponseDTO;
import com.controlacceso.accescontrol.service.RegistroSalidasEntradasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/horarios")
public class RegistroSalidasEntradasController {

    private final RegistroSalidasEntradasService registroService;

    public RegistroSalidasEntradasController(RegistroSalidasEntradasService registroService) {
        this.registroService = registroService;
    }

    @PostMapping("/registros")
    public RegistrosHorariosResponseDTO obtenerRegistros() {
        return registroService.obtenerRegistrosPorFiltro();
    }
}
