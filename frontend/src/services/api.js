import axios from 'axios';

// Clave con la que se guarda el token JWT en el navegador
export const CLAVE_TOKEN = 'tokenSonidoVivo';

// Cliente Axios central: todas las peticiones al backend pasan por aquí
// La URL viene del archivo .env (VITE_API_URL); si no existe, usa la de desarrollo
const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8081/api',
    headers: { 'Content-Type': 'application/json' }
});

// Antes de enviar cada petición, si hay un token guardado, lo agrega al encabezado
api.interceptors.request.use((config) => {
    const token = localStorage.getItem(CLAVE_TOKEN);
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// Convierte un error de Axios en un mensaje en español para mostrar al usuario
export const mensajeDeError = (error) => {
    const respuesta = error.response;
    if (!respuesta) {
        return 'No se pudo conectar con el servidor. Intenta más tarde.';
    }
    const datos = respuesta.data || {};
    // Errores de validación: se muestra el primero
    if (datos.detalles) {
        return Object.values(datos.detalles)[0];
    }
    return datos.error || 'Ocurrió un error inesperado.';
};

export default api;