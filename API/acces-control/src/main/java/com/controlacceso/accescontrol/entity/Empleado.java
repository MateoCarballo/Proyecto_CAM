package com.controlacceso.accescontrol.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Empleado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,length = 100)
    private String nombre;

    @Column(nullable = false,length = 100)
    private String apellidos;

    @Column(nullable = false)
    private Boolean activo;

    @Column(name = "fecha_creacion", nullable = false ,updatable = false)
    private String fechaCreacion;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL)
    private List<Tarjeta> tarjetas;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL)
    private List<Registro> registros;

    @OneToOne(mappedBy = "empleado", cascade = CascadeType.ALL)
    private UsuarioApp usuarioApp;
}
