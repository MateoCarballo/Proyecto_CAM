package com.controlacceso.accescontrol.controller;

import com.controlacceso.accescontrol.dto.RegistroCompletoHorariosDTO;
import com.controlacceso.accescontrol.dto.RegistroFiltroDTO;
import com.controlacceso.accescontrol.service.RegistroSalidasEntradasService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;
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
    public RegistroCompletoHorariosDTO getRegistrosPorFiltro(
            @RequestBody(required = false) RegistroFiltroDTO filtro,
            Authentication authentication) {

        // Extraer email autenticado desde el token JWT
        String emailAutenticado = authentication.getName();

        // Reemplazar el email del filtro por el del token (si viene nulo)
        if (filtro == null || filtro.email() == null) {
            filtro = new RegistroFiltroDTO(emailAutenticado, null, null);
        }

        return registroService.obtenerRegistrosPorEmail(filtro);
    }

    /* Antes de usar JWT
    @PostMapping("/registros")
    public RegistroCompletoHorariosDTO getRegistrosPorFiltro(@RequestBody RegistroFiltroDTO filtro) {
        return registroService.obtenerRegistrosPorEmail(filtro);
    }
     */

    // TODO: En el futuro, usar filtro.fechaInicio y filtro.fechaFin en el servicio

}
