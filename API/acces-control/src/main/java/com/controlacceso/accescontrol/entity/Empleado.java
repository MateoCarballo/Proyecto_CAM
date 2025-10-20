package com.controlacceso.accescontrol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "empleados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false,length = 100)
    private String nombre;

    @NotBlank
    @Column(nullable = false,length = 100)
    private String apellidos;

    @NotNull
    @Column(nullable = false)
    private boolean activo;

    @Column(name = "fecha_creacion", nullable = false ,updatable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany( mappedBy = "empleado")
    private List<Tarjeta> tarjetas;

    @OneToMany(mappedBy = "empleado")
    private List<Registro> registros;

    @OneToOne(
            mappedBy = "empleado",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private UsuarioApp usuarioApp;
}
