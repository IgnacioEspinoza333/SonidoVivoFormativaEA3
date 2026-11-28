-- Agrega a la tabla usuario los datos del perfil que ya usa el frontend (registro y perfil)
ALTER TABLE usuario
    ADD COLUMN nombre VARCHAR(255),
    ADD COLUMN apellido VARCHAR(255),
    ADD COLUMN rut VARCHAR(255) UNIQUE,
    ADD COLUMN email VARCHAR(255) UNIQUE,
    ADD COLUMN fecha_nacimiento DATE,
    ADD COLUMN instrumento VARCHAR(255),
    ADD COLUMN nivel VARCHAR(255);

-- Borra los usuarios de prueba antiguos; la aplicación crea los nuevos al arrancar
DELETE FROM usuario WHERE username IN ('admin', 'user');