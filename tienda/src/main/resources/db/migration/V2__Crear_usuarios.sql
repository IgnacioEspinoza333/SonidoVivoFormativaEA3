-- Tabla de usuarios del sistema (para el login con JWT)
-- rol puede ser 'USER' o 'ADMIN'
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(255) NOT NULL
);