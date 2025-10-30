package com.controlacceso.accescontrol.controller;

import com.controlacceso.accescontrol.dto.RegistroCompletoHorariosDTO;
import com.controlacceso.accescontrol.dto.RegistroFiltroDTO;
import com.controlacceso.accescontrol.service.RegistroSalidasEntradasService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/horarios")
public class RegistroSalidasEntradasController {
    private final RegistroSalidasEntradasService registroService;


    public RegistroSalidasEntradasController(RegistroSalidasEntradasService registroService) {
        this.registroService = registroService;
    }

    @PostMapping("/registros")
    public RegistroCompletoHorariosDTO getRegistrosPorFiltro(@RequestBody RegistroFiltroDTO filtro) {
        return registroService.obtenerRegistrosPorEmail(filtro);
    }

    // TODO: En el futuro, usar filtro.fechaInicio y filtro.fechaFin en el servicio

}
