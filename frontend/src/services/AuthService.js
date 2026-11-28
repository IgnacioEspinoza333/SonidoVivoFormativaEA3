// Servicio de autenticación: se comunica con el backend (Spring Boot) usando Axios.
// El token JWT y los datos del usuario se guardan en localStorage para mantener la sesión.
import api, { CLAVE_TOKEN, mensajeDeError } from './api';

export const CLAVE_SESION = 'usuarioActivo';
export const CLAVE_RECORDADO = 'usuarioRecordado';

// El backend usa los roles ADMIN y USER; el frontend usa admin y cliente.
// Este traductor se aplica al recibir los datos, así el resto de la app no cambia.
const ROLES_A_FRONTEND = { ADMIN: 'admin', USER: 'cliente' };
const aUsuarioFrontend = (usuario) => ({
    ...usuario,
    rol: ROLES_A_FRONTEND[usuario.rol] ?? 'cliente',
});

const leerJSON = (clave, porDefecto) => {
    try {
        const crudo = localStorage.getItem(clave);
        return crudo ? JSON.parse(crudo) : porDefecto;
    } catch {
        return porDefecto;
    }
};

// Envía usuario, correo y contraseña al backend.
// Si son correctos devuelve el usuario con su token; si no, devuelve null.
export async function verificarCredenciales({ usuario, email, password }) {
    try {
        const { data } = await api.post('/auth/login', { usuario, email, password });
        // El token viaja junto al usuario; AuthContext lo guarda aparte al iniciar sesión
        return { ...aUsuarioFrontend(data.usuario), token: data.token };
    } catch (error) {
        if (error.response && error.response.status === 401) {
            return null;
        }
        throw error;
    }
}

// Registra un usuario nuevo en el backend.
// Devuelve { ok: true, usuario } o { ok: false, error } con el mensaje a mostrar.
export async function registrarUsuario(datos) {
    try {
        const { data } = await api.post('/auth/register', {
            nombre: datos.nombre,
            apellido: datos.apellido,
            rut: datos.rut,
            usuario: datos.usuario,
            email: datos.email,
            fechaNacimiento: datos.fechaNacimiento,
            instrumento: datos.instrumento,
            nivel: datos.nivel,
            password: datos.password,
        });
        return { ok: true, usuario: aUsuarioFrontend(data) };
    } catch (error) {
        return { ok: false, error: mensajeDeError(error) };
    }
}

// --- Token y sesión guardados en el navegador ---
export const obtenerToken = () => localStorage.getItem(CLAVE_TOKEN);
export const guardarToken = (token) => localStorage.setItem(CLAVE_TOKEN, token);

// La sesión solo existe si hay token guardado
export const obtenerSesion = () => (obtenerToken() ? leerJSON(CLAVE_SESION, null) : null);
export const guardarSesion = (usuario) => localStorage.setItem(CLAVE_SESION, JSON.stringify(usuario));

// Al cerrar la sesión se borran el usuario y el token
export const borrarSesion = () => {
    localStorage.removeItem(CLAVE_SESION);
    localStorage.removeItem(CLAVE_TOKEN);
};

// "Recordarme": solo recuerda el nombre de usuario para precargarlo en el login
export const obtenerUsuarioRecordado = () => localStorage.getItem(CLAVE_RECORDADO) || '';
export const guardarUsuarioRecordado = (usuario, recordar) => {
    if (recordar) localStorage.setItem(CLAVE_RECORDADO, usuario.trim());
    else localStorage.removeItem(CLAVE_RECORDADO);
};