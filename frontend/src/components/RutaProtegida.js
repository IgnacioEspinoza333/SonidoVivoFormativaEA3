import React from 'react';
import { Navigate } from 'react-router-dom';
import AuthService from '../services/AuthService';

// Envuelve una pantalla y solo la muestra si hay sesión iniciada
// Con soloAdmin = true, además exige que el usuario sea ADMIN
const RutaProtegida = ({ children, soloAdmin = false }) => {
    if (!AuthService.isLoggedIn()) {
        return <Navigate to="/login" replace />;
    }
    if (soloAdmin && !AuthService.isAdmin()) {
        return <Navigate to="/" replace />;
    }
    return children;
};

export default RutaProtegida;