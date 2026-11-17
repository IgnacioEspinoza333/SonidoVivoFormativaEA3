import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import ProductoList from './components/ProductoList';
import ProductoForm from './components/ProductoForm';

function App() {
    return (
        <Router>
            <div style={{ padding: '20px' }}>
                <h1>Sistema de Gestión - Sonido Vivo</h1>
                <Routes>
                    <Route path="/" element={<ProductoList />} />
                    <Route path="/add" element={<ProductoForm />} />
                    <Route path="/edit/:id" element={<ProductoForm />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;