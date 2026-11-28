import { createContext, useState, useContext, useCallback } from 'react';
import {
  obtenerSesion,
  guardarSesion,
  guardarToken,
  borrarSesion,
  verificarCredenciales,
  registrarUsuario,
} from '../services/authService';

export const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export function AuthProvider({ children }) {
  // La sesión se restaura desde localStorage al cargar la app (sobrevive a recargas)
  const [usuario, setUsuario] = useState(() => obtenerSesion());

  // Recibe el usuario que devolvió el backend (incluye el token) y abre la sesión
  const iniciarSesion = useCallback((usuarioValido) => {
    const { token, ...datosUsuario } = usuarioValido;
    guardarToken(token);
    guardarSesion(datosUsuario);
    setUsuario(datosUsuario);
  }, []);

  const cerrarSesion = useCallback(() => {
    borrarSesion();
    setUsuario(null);
  }, []);

  const value = {
    usuario,
    estaAutenticado: Boolean(usuario),
    esAdmin: usuario?.rol === 'admin',
    verificarCredenciales,
    registrarUsuario,
    iniciarSesion,
    cerrarSesion,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}