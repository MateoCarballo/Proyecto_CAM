package com.controlacceso.accescontrol.controller;

import com.controlacceso.accescontrol.DTO.CardRequestDTO;
import com.controlacceso.accescontrol.DTO.CardResponseDTO;
import com.controlacceso.accescontrol.service.AccesControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
public class AccesControlController {
    private final AccesControlService accesControlService;

    @Autowired
    public AccesControlController(AccesControlService accesControlService) {
        this.accesControlService = accesControlService;
    }

    @PostMapping("/read")
    public ResponseEntity<CardResponseDTO> readCard(@RequestBody CardRequestDTO request){
        if(request.uid() == null || request.uid().isBlank()){
            return ResponseEntity.badRequest().body(
                    new CardResponseDTO(null,false,"El mensaje llega sin UID",null)
            );
        }
        return ResponseEntity.ok(accesControlService.proccesCard(request.uid()));
    }
}
