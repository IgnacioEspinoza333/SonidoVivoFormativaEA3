-- Carga el catálogo inicial: son los mismos 12 productos que tenía el frontend
-- Primero se vacía la tabla para que los IDs queden del 1 al 12
DELETE FROM producto;
ALTER TABLE producto AUTO_INCREMENT = 1;

INSERT INTO producto (id, nombre, categoria, marca, precio, stock, descripcion, imagen) VALUES
(1, 'Guitarra Eléctrica Stratocaster HSS', 'Guitarras', 'Fender', 349990, 5, 'Cuerpo en tilo, mástil de arce y pastillas HSS. Ideal para rock y blues.', '/img/guitarra2.jfif'),
(2, 'Guitarra Acústica Dreadnought', 'Guitarras', 'Yamaha', 189990, 8, 'Tapa de abeto macizo y aros de caoba. Sonido cálido y proyección amplia.', '/img/guitarras.jpg'),
(3, 'Bajo Eléctrico Jazz Bass 4 Cuerdas', 'Bajos', 'Fender', 299990, 4, 'Doble pastilla single coil, mástil delgado y gran versatilidad tonal.', '/img/guitarra1.jpg'),
(4, 'Piano Digital 88 Teclas Contrapesadas', 'Teclados', 'Roland', 459990, 3, 'Acción de martillo realista, sonidos de piano de concierto y pedal de sustain.', '/img/teclados.jpg'),
(5, 'Teclado Arranger 61 Teclas', 'Teclados', 'Casio', 259990, 6, 'Cientos de voces y ritmos preprogramados, ideal para práctica y presentaciones.', '/img/piano1.jfif'),
(6, 'Controlador MIDI 49 Teclas', 'Teclados', 'Akai', 129990, 7, 'Pads sensibles a la velocidad y knobs asignables, perfecto para producción musical.', '/img/piano2.2.jfif'),
(7, 'Batería Acústica 5 Piezas', 'Baterías', 'Pearl', 549990, 2, 'Set completo con platillos, herrajes y banco. Cáscaras de álamo para gran resonancia.', '/img/bateria1.jfif'),
(8, 'Batería Electrónica Compacta', 'Baterías', 'Alesis', 389990, 4, 'Módulo con más de 100 sonidos, ideal para práctica silenciosa con audífonos.', '/img/bateria2.jfif'),
(9, 'Set de Platillos 3 Piezas', 'Baterías', 'Zildjian', 99990, 9, 'Hi-hat, crash y ride en aleación de bronce B8 para un sonido brillante.', '/img/platos.jfif'),
(10, 'Amplificador de Guitarra 40W', 'Audio', 'Marshall', 149990, 6, 'Canales limpio y distorsión, reverb integrado y salida de auriculares.', '/img/caja1.1.jfif'),
(11, 'Micrófono de Estudio Condensador', 'Audio', 'Audio-Technica', 79990, 10, 'Patrón cardioide, ideal para grabación vocal y de instrumentos acústicos.', '/img/micro2.jfif'),
(12, 'Pedal Multiefectos', 'Audio', 'Boss', 119990, 5, 'Más de 50 efectos, looper integrado y afinador cromático incorporado.', '/img/pedal.jfif');