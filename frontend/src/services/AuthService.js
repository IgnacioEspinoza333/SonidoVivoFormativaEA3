import api from './api';

// Servicio de autenticación: login, cierre de sesión y datos de la sesión guardada
const AuthService = {

    // Envía usuario y contraseña al backend y guarda el token, el rol y el nombre
    login: async (username, password) => {
        const response = await api.post('/auth/login', { username, password });
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('rol', response.data.rol);
        localStorage.setItem('username', username);
        return response.data;
    },

    // Cierra la sesión borrando lo guardado
    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('rol');
        localStorage.removeItem('username');
    },

    getToken: () => localStorage.getItem('token'),

    getRol: () => localStorage.getItem('rol'),

    getUsername: () => localStorage.getItem('username'),

    isLoggedIn: () => localStorage.getItem('token') !== null,

    // Sirve solo para mostrar u ocultar botones. La seguridad real la hace el backend
    isAdmin: () => localStorage.getItem('rol') === 'ADMIN'
};

export default AuthService;