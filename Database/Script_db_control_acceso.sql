-- =========================================
-- SCRIPT COMPLETO DE PRUEBA CONTROL DE ACCESO PYME
-- Incluye roles, empleados, usuarios y registros
-- Ejecutable desde cero en cualquier PC
-- =========================================

-- 0️⃣ Limpiar y crear base de datos
DROP DATABASE IF EXISTS ControlAccesoPYME;
CREATE DATABASE ControlAccesoPYME;
USE ControlAccesoPYME;

-- =========================================
-- 1️⃣ Tablas
-- =========================================

-- Tabla empleados
CREATE TABLE empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla tarjetas RFID
CREATE TABLE tarjetas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(64) UNIQUE NOT NULL COMMENT 'UID único de la tarjeta RFID',
    empleado_id INT NOT NULL,
    fecha_activacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_desactivacion TIMESTAMP NULL,
    FOREIGN KEY (empleado_id) REFERENCES empleados(id) ON DELETE RESTRICT
);

-- Tabla registros de entrada/salida
CREATE TABLE registros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    empleado_id INT NOT NULL,
    fecha DATE NOT NULL DEFAULT (CURRENT_DATE),
    hora TIME NOT NULL DEFAULT (CURRENT_TIME),
    tipo ENUM('entrada', 'salida') NOT NULL,
    FOREIGN KEY (empleado_id) REFERENCES empleados(id) ON DELETE RESTRICT
);

-- Trigger para asegurar fecha/hora al insertar registros
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

-- Tabla roles (RBAC)
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) UNIQUE NOT NULL COMMENT 'Ej: admin, usuario'
);

-- Tabla usuarios de la app
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

-- =========================================
-- 2️⃣ Insertar roles
-- =========================================
INSERT INTO roles (nombre_rol) VALUES 
('admin'),
('usuario');

-- =========================================
-- 3️⃣ Insertar empleados
-- =========================================
INSERT INTO empleados (nombre, apellidos) VALUES
('Lewis', 'Hamilton'),
('Fernando', 'Alonso'),
('Sebastian', 'Vettel'),
('Max', 'Verstappen'),
('Admin', 'Root'); -- empleado de prueba admin

-- =========================================
-- 4️⃣ Insertar tarjetas RFID
-- =========================================
INSERT INTO tarjetas (uid, empleado_id) VALUES
('B70E5006', 1), -- Lewis Hamilton
('B3AB430E', 2), -- Fernando Alonso
('16194F06', 3), -- Sebastian Vettel
('F1D21953', 4), -- Max Verstappen
('ADMIN01', 5);  -- Admin Root

-- =========================================
-- 5️⃣ Insertar usuarios_app
-- Para simplificar, usar contraseñas hasheadas de ejemplo
-- bcrypt hash de "123456": $2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
-- =========================================
/* Por ahora todos se tienen que registrar como usuarios y luego le cambio a mano el rol de usuario a admin
INSERT INTO usuarios_app (empleado_id, email, hash_contrasena, rol_id) VALUES
(1, 'lewis.hamilton@mercedes.com', '$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX', 2),
(2, 'fernando.alonso@astonmartin.com', '$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX', 2),
(3, 'sebastian.vettel@ferrari.com', '$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX', 2),
(4, 'max.verstappen@redbull.com', '$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX', 2),
(5, 'admin@example.com', '$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX', 1); -- Admin
*/

-- =========================================
-- 6️⃣ Insertar registros de prueba
-- Para los días 30 y 31 de octubre para todos los empleados
-- =========================================

-- Día 30 de octubre
INSERT INTO registros (empleado_id, fecha, hora, tipo) VALUES
(1, '2025-10-30', '08:00:00', 'entrada'),
(1, '2025-10-30', '12:00:00', 'salida'),
(1, '2025-10-30', '13:00:00', 'entrada'),
(1, '2025-10-30', '17:00:00', 'salida'),

(2, '2025-10-30', '08:15:00', 'entrada'),
(2, '2025-10-30', '12:15:00', 'salida'),
(2, '2025-10-30', '13:15:00', 'entrada'),
(2, '2025-10-30', '17:15:00', 'salida'),

(3, '2025-10-30', '09:00:00', 'entrada'),
(3, '2025-10-30', '12:30:00', 'salida'),
(3, '2025-10-30', '13:30:00', 'entrada'),
(3, '2025-10-30', '18:00:00', 'salida'),

(4, '2025-10-30', '08:30:00', 'entrada'),
(4, '2025-10-30', '12:30:00', 'salida'),
(4, '2025-10-30', '13:30:00', 'entrada'),
(4, '2025-10-30', '17:30:00', 'salida'),

(5, '2025-10-30', '08:00:00', 'entrada'),
(5, '2025-10-30', '12:00:00', 'salida');

-- Día 31 de octubre
INSERT INTO registros (empleado_id, fecha, hora, tipo) VALUES
(1, '2025-10-31', '08:05:00', 'entrada'),
(1, '2025-10-31', '12:05:00', 'salida'),
(1, '2025-10-31', '13:05:00', 'entrada'),
(1, '2025-10-31', '17:05:00', 'salida'),

(2, '2025-10-31', '08:10:00', 'entrada'),
(2, '2025-10-31', '12:10:00', 'salida'),
(2, '2025-10-31', '13:10:00', 'entrada'),
(2, '2025-10-31', '17:10:00', 'salida'),

(3, '2025-10-31', '09:05:00', 'entrada'),
(3, '2025-10-31', '12:35:00', 'salida'),
(3, '2025-10-31', '13:35:00', 'entrada'),
(3, '2025-10-31', '18:05:00', 'salida'),

(4, '2025-10-31', '08:35:00', 'entrada'),
(4, '2025-10-31', '12:35:00', 'salida'),
(4, '2025-10-31', '13:35:00', 'entrada'),
(4, '2025-10-31', '17:35:00', 'salida'),

(5, '2025-10-31', '08:00:00', 'entrada'),
(5, '2025-10-31', '12:00:00', 'salida');

-- =========================================
-- 7️⃣ Consultas de prueba
-- =========================================
SELECT * FROM empleados;
SELECT * FROM tarjetas;
SELECT * FROM roles;
SELECT * FROM usuarios_app;
SELECT * FROM registros;
