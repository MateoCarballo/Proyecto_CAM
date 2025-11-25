-- Si existe, borramos la base de datos para no tener conflictos
DROP DATABASE IF EXISTS ControlAccesoPYME;

-- Creamos la base de datos desde cero
CREATE DATABASE ControlAccesoPYME;
USE ControlAccesoPYME;

-- =========================================
-- TABLA EMPLEADOS
-- Guardará los datos de cada trabajador
-- =========================================
CREATE TABLE empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================================
-- TABLA TARJETAS RFID
-- Cada tarjeta se asocia a un empleado
-- =========================================
CREATE TABLE tarjetas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(64) UNIQUE NOT NULL COMMENT 'UID único de la tarjeta RFID',
    empleado_id INT NOT NULL,
    fecha_activacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_desactivacion TIMESTAMP NULL,
    FOREIGN KEY (empleado_id) REFERENCES empleados(id) ON DELETE RESTRICT
);

-- =========================================
-- TABLA REGISTROS DE ENTRADA/SALIDA
-- Aquí guardamos cada asistencia
-- =========================================
CREATE TABLE registros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    empleado_id INT NOT NULL,
    fecha DATE NOT NULL DEFAULT (CURRENT_DATE),
    hora TIME NOT NULL DEFAULT (CURRENT_TIME),
    tipo ENUM('entrada','salida') NOT NULL,
    FOREIGN KEY (empleado_id) REFERENCES empleados(id) ON DELETE RESTRICT
);

-- Trigger para que si no metemos fecha/hora, se ponga la del momento automáticamente
DELIMITER $$
CREATE TRIGGER registros_before_insert
BEFORE INSERT ON registros
FOR EACH ROW
BEGIN
    IF NEW.fecha IS NULL THEN SET NEW.fecha = CURRENT_DATE; END IF;
    IF NEW.hora IS NULL THEN SET NEW.hora = CURRENT_TIME; END IF;
END $$
DELIMITER ;

-- =========================================
-- TABLA ROLES
-- Simple: admin o usuario
-- =========================================
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) UNIQUE NOT NULL
);

-- =========================================
-- TABLA USUARIOS_APP
-- Aquí guardamos las credenciales de la app
-- Importante: la contraseña se guarda hasheada
-- =========================================
CREATE TABLE usuarios_app (
    id INT AUTO_INCREMENT PRIMARY KEY,
    empleado_id INT UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    hash_contrasena VARCHAR(255) NOT NULL COMMENT 'Contraseña hasheada',
    rol_id INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (empleado_id) REFERENCES empleados(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);


