-- Eliminar la base de datos si existe
DROP DATABASE IF EXISTS 5_6_7_9_ayd2_db;

-- Crear la base de datos
CREATE DATABASE 5_6_7_9_ayd2_db;

-- Usar la base de datos
USE 5_6_7_9_ayd2_db;

-- Verificar si el usuario no existe y crearlo
CREATE USER IF NOT EXISTS 'user_5_6_7_9_ayd2_db'@'localhost' IDENTIFIED BY '123';

-- Otorgar permisos específicos sobre la base de datos
GRANT ALL PRIVILEGES ON 5_6_7_9_ayd2_db.* TO 'user_5_6_7_9_ayd2_db'@'localhost';

-- Aplicar cambios de permisos
FLUSH PRIVILEGES;