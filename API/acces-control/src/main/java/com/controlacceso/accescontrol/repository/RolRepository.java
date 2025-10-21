package com.controlacceso.accescontrol.repository;

import com.controlacceso.accescontrol.entity.Empleado;
import com.controlacceso.accescontrol.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol,Integer> {
    Optional<Rol> findByNombreRol(String nombreRol);
}
