import axios from 'axios';

// Cliente Axios central: todas las peticiones al backend pasan por aquí
const api = axios.create({
    baseURL: 'http://localhost:8081/api',
    headers: { 'Content-Type': 'application/json' }
});

// Antes de enviar cada petición, si hay un token guardado, lo agrega al encabezado
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// Al recibir la respuesta: si el backend dice 401 (token vencido o inválido),
// se cierra la sesión y se vuelve al login. El 401 del propio login no cuenta,
// porque ahí significa "usuario o contraseña incorrectos".
api.interceptors.response.use(
    (response) => response,
    (error) => {
        const esLogin = error.config && error.config.url && error.config.url.includes('/auth/login');
        if (error.response && error.response.status === 401 && !esLogin) {
            localStorage.removeItem('token');
            localStorage.removeItem('rol');
            localStorage.removeItem('username');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default api;