package com.controlacceso.accescontrol.repository;

import com.controlacceso.accescontrol.entity.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistroRepository extends JpaRepository<Registro,Integer> {
    Optional<Registro> findTopByEmpleadoIdOrderByFechaHoraDesc(Integer empleadoId);
}
