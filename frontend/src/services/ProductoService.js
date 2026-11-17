import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ProductoService from '../services/ProductoService';

const ProductoForm = () => {
    const [nombre, setNombre] = useState('');
    const [precio, setPrecio] = useState('');
    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if (id) {
            ProductoService.getProductoById(id).then(response => {
                setNombre(response.data.nombre);
                setPrecio(response.data.precio);
            });
        }
    }, [id]);

    const saveOrUpdateProducto = (e) => {
        e.preventDefault();
        const producto = { nombre, precio: parseFloat(precio) };

        if (id) {
            ProductoService.updateProducto(id, producto).then(() => navigate('/'));
        } else {
            ProductoService.createProducto(producto).then(() => navigate('/'));
        }
    };

    return (
        <div>
            <h2>{id ? 'Editar Producto' : 'Agregar Producto'}</h2>
            <form onSubmit={saveOrUpdateProducto}>
                <div>
                    <label>Nombre del instrumento:</label>
                    <input type="text" value={nombre} onChange={(e) => setNombre(e.target.value)} required />
                </div>
                <div style={{ marginTop: '10px' }}>
                    <label>Precio:</label>
                    <input type="number" value={precio} onChange={(e) => setPrecio(e.target.value)} required />
                </div>
                <button type="submit" style={{ marginTop: '10px' }}>{id ? 'Actualizar' : 'Guardar'}</button>
            </form>
        </div>
    );
};

export default ProductoForm;