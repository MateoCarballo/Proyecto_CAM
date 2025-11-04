package com.controlacceso.accescontrol.service;

import com.controlacceso.accescontrol.dto.CardResponseDTO;
import com.controlacceso.accescontrol.entity.Registro;
import com.controlacceso.accescontrol.entity.Tarjeta;
import com.controlacceso.accescontrol.repository.RegistroRepository;
import com.controlacceso.accescontrol.repository.TarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccesControlService {
    private final TarjetaRepository tarjetaRepositoryImpl;
    private final RegistroRepository registroRepositoryImpl ;

    @Autowired
    public AccesControlService(TarjetaRepository tarjetaRepositoryImpl, RegistroRepository registroRepositoryImpl) {
        this.tarjetaRepositoryImpl = tarjetaRepositoryImpl;
        this.registroRepositoryImpl = registroRepositoryImpl;
    }

    public CardResponseDTO proccesCard(String uid) {
        // Está registrada la tarjeta ?
        Optional<Tarjeta> cardRequestOptional = tarjetaRepositoryImpl.findByUid(uid);
        if (cardRequestOptional.isEmpty()){
            return new CardResponseDTO(
                    uid,
                    false,
                    "Tarjeta no registrada",
                    LocalDateTime.now());
        }
        Tarjeta card = cardRequestOptional.get();
        if (!card.getEmpleado().isActivo() || card.getFechaDesactivacion() != null){
            return new CardResponseDTO(
                    uid,
                    false,
                    "Tarjeta o empleado inactivo",
                    LocalDateTime.now());

        }
        // Tiene registros? Si los tiene cual es el último ?
        Optional<Registro> ultimoRegistroOpt = registroRepositoryImpl.findTopByEmpleadoIdOrderByFechaDescHoraDesc(card.getEmpleado().getId());
        Registro.TipoRegistro nuevoTipo;
        if (ultimoRegistroOpt.isEmpty()) {
            nuevoTipo = Registro.TipoRegistro.entrada;
        } else {
            Registro.TipoRegistro ultimoTipo = ultimoRegistroOpt.get().getTipo();
            nuevoTipo = (ultimoTipo == Registro.TipoRegistro.entrada)
                    ? Registro.TipoRegistro.salida
                    : Registro.TipoRegistro.entrada;
        }

        //Guardar lo contrario al ultimo registro

        Registro nuevoRegistro = Registro.builder()
                .empleado(card.getEmpleado())
                .tipo(nuevoTipo)
                .build();

        registroRepositoryImpl.save(nuevoRegistro);

        return new CardResponseDTO(
                uid,
                true,
                "Acceso registrado correctamente (" + nuevoTipo + ")",
                LocalDateTime.now()
        );
    }
}
