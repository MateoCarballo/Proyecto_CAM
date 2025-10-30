-- Crear la base de datos
DROP DATABASE IF EXISTS ControlAccesoPYME;
CREATE DATABASE ControlAccesoPYME;
USE ControlAccesoPYME;

-- Tabla para almacenar la información de los empleados
CREATE TABLE empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla para gestionar las tarjetas RFID
CREATE TABLE tarjetas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(64) UNIQUE NOT NULL COMMENT 'UID único de la tarjeta RFID',
    empleado_id INT NOT NULL,
    fecha_activacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_desactivacion TIMESTAMP NULL,
    FOREIGN KEY (empleado_id) REFERENCES empleados(id) ON DELETE RESTRICT
);

-- Tabla para registrar todos los eventos de entrada y salida
CREATE TABLE registros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    empleado_id INT NOT NULL,
    fecha DATE NOT NULL DEFAULT (CURRENT_DATE),
    hora TIME NOT NULL DEFAULT (CURRENT_TIME),
    tipo ENUM('entrada', 'salida') NOT NULL,
    FOREIGN KEY (empleado_id) REFERENCES empleados(id) ON DELETE RESTRICT
);


-- Tabla de roles para el control de acceso en la app (RBAC)
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) UNIQUE NOT NULL COMMENT 'Ej: admin, usuario'
);

-- Tabla para los usuarios de la aplicación Android
CREATE TABLE usuarios_app (
    id INT AUTO_INCREMENT PRIMARY KEY,
    empleado_id INT UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    hash_contrasena VARCHAR(255) NOT NULL COMMENT 'Almacenar contraseña hasheada',
    rol_id INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (empleado_id) REFERENCES empleados(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

DELIMITER $$

CREATE TRIGGER registros_before_insert
BEFORE INSERT ON registros
FOR EACH ROW
BEGIN
    IF NEW.fecha IS NULL THEN
        SET NEW.fecha = CURRENT_DATE;
    END IF;
    IF NEW.hora IS NULL THEN
        SET NEW.hora = CURRENT_TIME;
    END IF;
END $$

DELIMITER ;

-- Insertar roles básicos del sistema
INSERT INTO roles (nombre_rol) VALUES 
('admin'),
('usuario');

-- Datos de ejemplo
INSERT INTO empleados (nombre, apellidos) VALUES
('Lewis', 'Hamilton'),
('Fernando', 'Alonso'),
('Sebastian', 'Vettel'),
('Max', 'Verstappen');

INSERT INTO tarjetas (uid, empleado_id) VALUES
('B70E5006', 1), -- Lewis Hamilton
('B3AB430E', 2), -- Fernando Alonso
('16194F06', 3), -- Sebastian Vettel
('F1D21953', 4); -- Max Verstappen 

