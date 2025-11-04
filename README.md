<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
</head>
<body>

<h1>Proyecto Control de Acceso - CAM</h1>
<p>Este repositorio centraliza todos los componentes del proyecto de control de acceso:</p>

<<div class="card">
<h2>1️⃣ API - Backend Spring Boot</h2>
<p>Proporciona la lógica del negocio, endpoints REST para login, registro, gestión de empleados y registros horarios.</p>
<ul>
<li><a href="./backend">Carpeta Backend</a></li>
<li>Java 17+, Maven, MySQL</li>
<li>EndPoints principales:
  <ul>
    <li><code>POST /auth/login</code> - Login de usuario (JWT)</li>
    <li><code>POST /auth/register</code> - Registro de usuario</li>
    <li><code>GET /horarios/registros</code> - Obtener registros horarios filtrados (según rol: admin todos, usuario solo propios)</li>
    <li><code>POST /card/read</code> - Registrar entrada/salida automática vía RFID
        <ul>
            <li>Autenticación mediante <code>API-Key</code> en header</li>
            <li>Recibe UID de tarjeta y determina empleado</li>
            <li>Inserta un registro en la tabla <code>registros</code> con tipo <code>entrada</code> o <code>salida</code> según última acción</li>
            <li>Respuesta: JSON con estado del registro y timestamp</li>
        </ul>
    </li>
  </ul>
</li>
<li>Autenticación con <code>Bearer Token</code> JWT para usuarios y <code>API-Key</code> para lector RFID</li>
<li>Pruebas recomendadas: Postman</li>
</ul>
</div>

<div class="card">
<h2>2️⃣ Firmware - Arduino</h2>
<p>Microcontrolador encargado de leer tarjetas RFID y enviar eventos a la API REST.</p>
<ul>
<li><a href="./firmware-arduino">Carpeta Firmware Arduino</a></li>
<li>Librerías requeridas: <code>MFRC522</code>, <code>SPI</code>, <code>WiFi</code> (según módulo)</li>
<li>Configuración de red y URL de API en <code>main.ino</code></li>
<li>Logs por Serial y UID de tarjetas</li>
<li>Buenas prácticas: versionado de librerías, pruebas en entorno de test</li>
</ul>
</div>

<div class="card">
<h2>3️⃣ App Android</h2>
<p>Aplicación móvil para que los administradores y usuarios puedan consultar los registros horarios y gestionar empleados.</p>
<ul>
<li><a href="./android-app">Carpeta Android App</a></li>
<li>Java/Kotlin, Retrofit para consumir la API</li>
<li>Interfaz adaptativa según rol (Admin ve todos los registros, Usuario solo los suyos)</li>
<li>Posibilidad de filtrado por empleado y rango de fechas</li>
</ul>
</div>

<div class="card">
<h2>4️⃣ Base de Datos</h2>
<p>Script SQL para creación de la base de datos y datos de prueba.</p>
<ul>
<li><a href="./Database">Carpeta Database</a></li>
<li>MySQL 8+</li>
<li>Contiene tablas: <code>empleados</code>, <code>tarjetas</code>, <code>registros</code>, <code>roles</code>, <code>usuarios_app</code></li>
<li>Triggers para insertar fecha/hora automáticamente</li>
<li>Datos de prueba para empleados, tarjetas, registros y usuarios</li>
</ul>
</div>

<div class="card">
<h2>5️⃣ Buenas prácticas</h2>
<ul>
<li>Mantener repositorios separados por módulo cuando el proyecto crece (monorepo vs multirepo)</li>
<li>Documentar endpoints y firmware en Markdown</li>
<li>Usar DTOs para separar entidad y representación de datos</li>
<li>Versionar API y firmware mediante tags</li>
<li>Probar primero en entorno local antes de producción</li>
</ul>
</div>

<div class="card">
<h2>6️⃣ Estructura recomendada</h2>
<pre>
ProyectoCAM/
├─ backend/           -> API Spring Boot
├─ firmware-arduino/  -> Firmware microcontrolador
├─ android-app/       -> App Android
├─ Database/          -> Scripts SQL y datos de prueba
└─ README.md          -> Este archivo
</pre>
</div>

<p>Este README centralizado sirve como guía inicial y punto de acceso a toda la documentación de los diferentes módulos.</p>

</body>
</html>
# Proyecto_CAM
