package com.controlacceso.accescontrol.service;

import com.controlacceso.accescontrol.repository.AccesControlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccesControlService {
    private final AccesControlRepository accesControlRepositoryImpl;

    @Autowired
    public AccesControlService(AccesControlRepository accesControlRepositoryImpl) {
        this.accesControlRepositoryImpl = accesControlRepositoryImpl;
    }
}
