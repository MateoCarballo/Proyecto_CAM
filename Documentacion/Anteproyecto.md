<!-- Portada oficial anteproyecto FCT DAM - Mateo Carballo Alonso -->
<!-- Diseñada según criterios académicos y profesionales para presentación en Vigo -->

<div style="text-align: center; font-family: Arial, sans-serif; line-height: 1.5;">

<br><br><br>

<h2 style="margin-bottom: 0;">IES de Teis – Vigo</h2>
<h3 style="margin-top: 4px; font-weight: normal;">
Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)
</h3>
<h3 style="font-weight: normal;">Módulo: Formación en Centros de Trabajo (FCT)</h3>
<h3 style="font-weight: normal;">Curso académico 2025–2026</h3>

<br><br><br>



<h2 style="font-size: 24px; margin-bottom: 0;">ANTEPROYECTO DE FCT</h2>

<h1 style="font-size: 32px; margin-top: 10px;">
SISTEMA INTEGRAL DE CONTROL DE ACCESO Y GESTIÓN DE HORARIOS CON RFID, ESP32, API-REST Y APLICACIÓN MÓVIL
</h1>

<br><br><br>

<p style="font-size: 16px;">
<b>Autor:</b> Mateo Carballo Alonso<br>
<b>Tutor académico:</b> <br>
<b>Tutor de empresa:</b> Santiago Reyes<br>
<b>Centro de trabajo:</b> Excentria <br>
<b>Fecha:</b> Vigo, octubre de 2025
</p>

<br><br><br>

<!-- Logotipos opcionales -->
<!-- Logotipos opcionales -->
<div style="display: flex; justify-content: space-between; align-items: center; margin-top: 40px; max-width: 80%; margin-left: auto; margin-right: auto;">
    <img src="https://upload.wikimedia.org/wikipedia/commons/6/6e/Logo_da_Xunta_de_Galicia_%282021%29.svg"
         alt="Logo Xunta de Galicia" width="25%">
    <img src="https://upload.wikimedia.org/wikipedia/commons/6/63/EscudoVigo.svg"
         alt="Escudo de Vigo" width="10%">
    <!-- Puedes añadir el logo del centro educativo o de la empresa si lo deseas -->
</div>

<br><br><br>

<p style="font-size: 12px; color: #666;">
Este documento forma parte del anteproyecto presentado , conforme a los criterios establecidos por el IES de Tesi – Vigo.
</p>

</div>

<br><br>


# Anteproyecto de FCT - Sistema de Control de Acceso con RFID

## Título del Proyecto:
**Sistema Integral de Control de Acceso y Gestión de Horarios con RFID, ESP32 y Aplicación Móvil**

**Autor:** Mateo Carballo Alonso  
**Ciclo:** Desarrollo de Aplicaciones Multiplataforma (DAM)  
**Curso académico:** 2025

---

## 1. Descripción General Ampliada

El proyecto consiste en el desarrollo de un sistema completo de control de acceso y gestión horaria para pequeñas y medianas empresas. El sistema integra:

- **Hardware:** Dispositivos ESP32 con módulo CP210x (WiFi/Bluetooth) y lectores RFID RC522 distribuidos en puntos de acceso.
- **Backend:** API REST desarrollada con Spring Boot que gestiona la lógica de negocio.
- **Frontend Móvil:** Aplicación Android para consulta de registros en tiempo real.
- **Base de Datos:** MySQL para almacenamiento persistente de todos los datos del sistema.

Cada empleado dispondrá de una tarjeta RFID personal que, al ser leída en cualquier terminal, registrará automáticamente su entrada/salida. Los datos se sincronizarán mediante WiFi con el servidor central y estarán disponibles para consulta inmediata a través de la aplicación móvil.

## 2. Objetivos Específicos del Proyecto

### 2.1. Objetivos de Hardware
- Configurar ESP32 con módulo CP210x para comunicación WiFi estable.
- Implementar protocolo de lectura RFID fiable con módulo RC522.
- Diseñar circuito robusto para instalación permanente.
- Garantizar autonomía energética y estabilidad de conexión.

### 2.2. Objetivos de Backend (Spring Boot)
- Desarrollar API REST segura con autenticación JWT.
- Implementar arquitectura en capas (Controller-Service-Repository).
- Configurar conexión a base de datos MySQL con Spring Data JPA.
- Implementar validación de datos y manejo de excepciones.
- Generar endpoints para:
  - Registro de eventos RFID.
  - Autenticación de usuarios móviles.
  - Consulta de registros históricos.
  - Gestión administrativa.

### 2.3. Objetivos de Aplicación Móvil
- Desarrollar interfaz intuitiva para Android.
- Implementar sistema de autenticación seguro.
- Crear dashboard personalizado según rol de usuario.
  - **Usuario estándar:** Visualización exclusiva de sus propios registros.
  - **Usuario administrador:** Acceso completo a todos los registros con filtros avanzados.
- Garantizar visualización en tiempo real de los registros.
- Implementar funcionalidad de exportación de reportes.

## 3. Arquitectura Técnica Detallada

### 3.1. Stack Tecnológico Completado

**Hardware:**
- Microcontrolador: ESP32 con módulo CP210x.
- Comunicación: WiFi 802.11 b/g/n.
- Lector RFID: RC522 (13.56 MHz).
- Alimentación: 5V DC.

**Backend:**
- Framework: Spring Boot 3.x.
- Gestión de dependencias: Maven.
- Java: Versión 17.
- Persistencia: Spring Data JPA + Hibernate.
- Base de datos: MySQL 8.0.
- Seguridad: Spring Security + JWT.
- Documentación: OpenAPI 3.0.

**Aplicación Móvil:**
- Plataforma: Android (Java).
- Arquitectura: MVVM.
- Comunicación: Retrofit2 + OkHttp3.
- Persistencia local: Room Database.


## 4. Funcionalidades por Módulo

### 4.1. Módulo de Captura (ESP32)
- Lectura continua de tarjetas RFID.
- Detección automática entrada/salida (lógica timestamp).
- Envío HTTP POST a endpoints REST.
- Reconexión automática en caso de pérdida de WiFi.
- Indicación visual (LED) de estado operativo.

### 4.2. Módulo Backend (Spring Boot)

Endpoints Principales:
- `POST /api/registros/rfid` → Registro eventos desde ESP32.
- `POST /api/auth/login` → Autenticación app móvil.
- `GET /api/registros/usuario/{id}` → Registros por usuario.
- `GET /api/registros/admin` → Todos los registros (admin).
- `GET /api/empleados` → Gestión de empleados.
- `GET /api/reportes/horas` → Generación de reportes.

### 4.3. Módulo Aplicación Móvil

**Para usuarios estándar:**
- Login con credenciales personales.
- Dashboard con registros del día actual.
- Histórico personal con filtros por fecha.
- Visualización de horas totales trabajadas.

**Para usuarios administradores:**
- Vista general de toda la plantilla.
- Filtros avanzados por empleado, fecha, departamento.
- Exportación de reportes en PDF/Excel.
- Gestión básica de usuarios y tarjetas.

## 5. Modelo de Datos Implementado

La base de datos, tal como se ha definido, incluye:

- **empleados:** Información personal de cada trabajador.
- **tarjetas:** Vinculación RFID-Empleado con control de estado.
- **registros:** Traza completa de todos los eventos.
- **roles:** Sistema RBAC para control de acceso en la app.
- **usuarios_app:** Credenciales para acceso móvil.

## 6. Plan de Desarrollo por Sprints

**Sprint 1:** Configuración inicial ESP32 + Comunicación básica.  
**Sprint 2:** Desarrollo API Spring Boot + Conexión MySQL.  
**Sprint 3:** Implementación autenticación JWT + Seguridad.  
**Sprint 4:** Desarrollo aplicación móvil (usuario estándar).  
**Sprint 5:** Desarrollo módulo administrador + Reportes.  
**Sprint 6:** Integración completa + Pruebas de estrés.  
**Sprint 7:** Documentación + Preparación defensa.  

<br><br><br>

## 7. Criterios de Aceptación

- Sistema debe registrar eventos RFID con latencia < 2 segundos.
- Aplicación móvil debe mostrar registros con máximo 5 segundos de desfase.
- API debe soportar mínimo 50 peticiones concurrentes.
- Autenticación móvil debe ser 100% segura.
- Interfaz administrativa debe permitir exportación de datos.

## 8. Valor Añadido del Proyecto

Este proyecto destaca por:
- **Integración completa** hardware-software-móvil.
- **Arquitectura escalable** que permite ampliación futura.
- **Enfoque real** para problemática empresarial existente.
- **Tecnologías actuales** utilizadas en entorno profesional.
- **Documentación técnica completa** para mantenimiento.

---