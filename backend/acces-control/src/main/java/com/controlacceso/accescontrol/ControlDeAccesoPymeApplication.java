package com.controlacceso.accescontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		scanBasePackages = "com.controlacceso.accescontrol"
)
public class ControlDeAccesoPymeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControlDeAccesoPymeApplication.class, args);
	}
	//TODO JOSE Cómo deberia estructurar la API a la hora de "cifrar" la entrada de peticiones a los endPoints. Sobre todo a los pensados en la app android
	//TODO JOSE Como puedo asegurarme que solo usuarios de la app puedan hacer peticiones a la API? La idea es que la informacion sea accesible para movil y pc.
	// Mi idea es que los usuarios acceden con su correo y contraseña (la contraseña encriptada). La app tendra una pantalla de login con otro boton para poder registrarnos.
	// Si no estamos registrados introducir el numero de empleado y la contraseña dos veces. Y desde ahi hacer el registro si el numero de usuario existe y no esta registrado.
	// Si estamos registrados tendremos nuestro email y lo buscará junto con nuestra clave para probar acceso.
	// Al entrar si este tiene un rol de "usuario" podrá ver todas sus entradas y salidas .Si tiene un rol de "administrador" entonces podrá ver la de todos.
	// En necesario la paginación, como la aplico? Desde la app o desde aquí ?
	// Está bien que siempre devuleva lo mismo, una lista de empleados, que si es rol admin  tendra varios objetos en la lista y si es rol usuario solo tendrá uno ?
}
