-- CREAR UN USUARIO PARA LA BASE DE DATOS SINTAXIS
-- CREATE USER 'username'@'host' IDENTIFIED BY 'password'

CREATE USER 'api_java'@'%' IDENTIFIED BY 'UnaContrasenaMuySegura123!';
GRANT SELECT, INSERT ON ControlAccesoPYME.registros TO 'api_java'@'%';
GRANT SELECT ON ControlAccesoPYME.empleados TO 'api_java'@'%';
GRANT SELECT ON ControlAccesoPYME.tarjetas TO 'api_java'@'%';
GRANT SELECT ON ControlAccesoPYME.usuarios_app TO 'api_java'@'%';
FLUSH PRIVILEGES;