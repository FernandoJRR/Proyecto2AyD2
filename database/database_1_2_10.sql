-- Eliminar la base de datos si existe
DROP DATABASE IF EXISTS 1_2_10_ayd2_db;

-- Crear la base de datos
CREATE DATABASE 1_2_10_ayd2_db;

-- Usar la base de datos
USE 1_2_10_ayd2_db;

-- Verificar si el usuario no existe y crearlo
CREATE USER IF NOT EXISTS 'user_1_2_10_ayd2_db'@'localhost' IDENTIFIED BY '123';

-- Otorgar permisos específicos sobre la base de datos
GRANT ALL PRIVILEGES ON 1_2_10_ayd2_db.* TO 'user_1_2_10_ayd2_db'@'localhost';

-- Aplicar cambios de permisos
FLUSH PRIVILEGES;