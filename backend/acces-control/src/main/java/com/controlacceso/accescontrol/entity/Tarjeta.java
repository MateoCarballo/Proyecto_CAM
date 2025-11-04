package com.controlacceso.accescontrol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarjetas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(nullable = false, unique = true, length = 64)
    private String uid;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @Column(name = "fecha_activacion", nullable = false)
    private LocalDateTime fechaActivacion = LocalDateTime.now();

    @Column(name = "fecha_desactivacion")
    private LocalDateTime fechaDesactivacion;
}
