package com.controlacceso.accescontrol.repository;

import com.controlacceso.accescontrol.entity.Tarjeta;
import com.controlacceso.accescontrol.entity.UsuarioApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioAppRepository extends JpaRepository<UsuarioApp, Integer> {
    Optional<UsuarioApp> findByEmail(String email);
}
