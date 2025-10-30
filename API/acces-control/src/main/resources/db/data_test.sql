USE ControlAccesoPYME;

-- ========================
-- LIMPIAR DATOS EXISTENTES
-- ========================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE usuarios_app;
TRUNCATE TABLE registros;
TRUNCATE TABLE tarjetas;
TRUNCATE TABLE empleados;
TRUNCATE TABLE roles;
SET FOREIGN_KEY_CHECKS = 1;

-- =====================
-- INSERTAR ROLES
-- =====================
INSERT INTO roles (nombre_rol) VALUES
('admin'),
('usuario');

-- =====================
-- INSERTAR EMPLEADOS
-- =====================
INSERT INTO empleados (nombre, apellidos, activo) VALUES
('Lewis', 'Hamilton', TRUE),
('Fernando', 'Alonso', TRUE),
('Sebastian', 'Vettel', TRUE),
('Max', 'Verstappen', TRUE);

-- =====================
-- INSERTAR TARJETAS RFID
-- =====================
INSERT INTO tarjetas (uid, empleado_id) VALUES
('B70E5006', 1),  -- Lewis Hamilton
('B3AB430E', 2),  -- Fernando Alonso
('16194F06', 3),  -- Sebastian Vettel
('F1D21953', 4);  -- Max Verstappen

-- =====================
-- INSERTAR USUARIOS DE APP (para login JWT)
-- =====================
-- Contraseña usada: abc123
-- Hash generado con BCrypt (usa BCrypt.gensalt() en Java)
INSERT INTO usuarios_app (empleado_id, email, hash_contrasena, rol_id) VALUES
(2, 'fernando.alonso@email.com', '$2a$10$EoU8xT9EbgcQwvPbCrf/8e6Mu0xgm2fXZwW9q2vIGp4q2vX3J/5b2', 2), -- Usuario normal
(1, 'lewis.hamilton@email.com', '$2a$10$EoU8xT9EbgcQwvPbCrf/8e6Mu0xgm2fXZwW9q2vIGp4q2vX3J/5b2', 1);  -- Admin

-- =====================
-- INSERTAR REGISTROS DE EJEMPLO
-- =====================
INSERT INTO registros (empleado_id, fecha, hora, tipo) VALUES
(2, CURDATE(), '08:00:00', 'entrada'),
(2, CURDATE(), '13:30:00', 'salida'),
(2, CURDATE(), '15:00:00', 'entrada'),
(2, CURDATE(), '18:00:00', 'salida');
