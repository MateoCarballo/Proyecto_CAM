package com.controlacceso.accescontrol.controller;

import com.controlacceso.accescontrol.DTO.RegistroCompletoHorariosDTO;
import com.controlacceso.accescontrol.service.RegistroSalidasEntradasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/horarios")
public class RegistroSalidasEntradasController {
    private final RegistroSalidasEntradasService registroService;


    public RegistroSalidasEntradasController(RegistroSalidasEntradasService registroService) {
        this.registroService = registroService;
    }

    @GetMapping("/registros")
    public RegistroCompletoHorariosDTO getRegistrosPorEmail(@RequestParam String email) {
        return registroService.obtenerRegistrosPorEmail(email);
    }
    //TODO mejora! Usar POST para poder enviar datos de filtrado si los tenemos. Filtrar por fechas por ejemplo
    
}
