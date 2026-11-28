import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate, useNavigate, useLocation } from 'react-router-dom';
import Login from './components/Login';
import RutaProtegida from './components/RutaProtegida';
import ProductoList from './components/ProductoList';
import ProductoForm from './components/ProductoForm';
import AuthService from './services/AuthService';

// Encabezado con el título, el usuario conectado y el botón para cerrar sesión
const Encabezado = () => {
    const navigate = useNavigate();
    useLocation(); // hace que el encabezado se actualice cada vez que cambia la pantalla

    const cerrarSesion = () => {
        AuthService.logout();
        navigate('/login');
    };

    return (
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <h1>Sistema de Gestión - Sonido Vivo</h1>
            {AuthService.isLoggedIn() && (
                <div>
                    <span>Usuario: <strong>{AuthService.getUsername()}</strong> ({AuthService.getRol()})</span>
                    <button onClick={cerrarSesion} style={{ marginLeft: '10px' }}>Cerrar sesión</button>
                </div>
            )}
        </div>
    );
};

function App() {
    return (
        <Router>
            <div style={{ padding: '20px' }}>
                <Encabezado />
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/" element={
                        <RutaProtegida>
                            <ProductoList />
                        </RutaProtegida>
                    } />
                    {/* Crear y editar: solo ADMIN */}
                    <Route path="/add" element={
                        <RutaProtegida soloAdmin>
                            <ProductoForm />
                        </RutaProtegida>
                    } />
                    <Route path="/edit/:id" element={
                        <RutaProtegida soloAdmin>
                            <ProductoForm />
                        </RutaProtegida>
                    } />
                    {/* Cualquier otra dirección vuelve al inicio */}
                    <Route path="*" element={<Navigate to="/" replace />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;