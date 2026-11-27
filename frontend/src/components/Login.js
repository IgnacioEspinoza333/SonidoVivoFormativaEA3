import React, { useState } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import AuthService from '../services/AuthService';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    // Si ya hay una sesión iniciada, no tiene sentido mostrar el login
    if (AuthService.isLoggedIn()) {
        return <Navigate to="/" replace />;
    }

    const iniciarSesion = (e) => {
        e.preventDefault();
        setError('');

        AuthService.login(username, password)
            .then(() => navigate('/'))
            .catch((err) => {
                if (err.response && err.response.status === 401) {
                    setError('Usuario o contraseña incorrectos');
                } else {
                    setError('No se pudo conectar con el servidor');
                }
            });
    };

    return (
        <div>
            <h2>Iniciar sesión</h2>
            <form onSubmit={iniciarSesion}>
                <div>
                    <label>Usuario:</label>
                    <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} required />
                </div>
                <div style={{ marginTop: '10px' }}>
                    <label>Contraseña:</label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                </div>
                <button type="submit" style={{ marginTop: '10px' }}>Entrar</button>
            </form>
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
};

export default Login;