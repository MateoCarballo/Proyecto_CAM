package com.controlacceso.accescontrol.controller;

import com.controlacceso.accescontrol.service.AccesControlService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
