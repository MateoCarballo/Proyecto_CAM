package com.controlacceso.accescontrol.service;

import com.controlacceso.accescontrol.DTO.CardResponseDTO;
import com.controlacceso.accescontrol.repository.AccesControlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccesControlService {
    private final AccesControlRepository accesControlRepositoryImpl;

    @Autowired
    public AccesControlService(AccesControlRepository accesControlRepositoryImpl) {
        this.accesControlRepositoryImpl = accesControlRepositoryImpl;
    }

    public CardResponseDTO proccesCard(String uid) {
        boolean authorized = validateAcces(uid);
        return new CardResponseDTO(
                uid,
                authorized,
                authorized ? "Acceso permitido" : "Acceso denegado",
                LocalDateTime.now()
        );
    }

    private boolean validateAcces(String uid) {
        return uid.equalsIgnoreCase("F6053F06");
    }
}
