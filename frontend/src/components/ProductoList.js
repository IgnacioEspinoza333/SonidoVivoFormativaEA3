import React, { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import ProductoService from '../services/ProductoService';
import AuthService from '../services/AuthService';

const ProductoList = () => {
    const [productos, setProductos] = useState([]);
    const [mensaje, setMensaje] = useState('');
    const esAdmin = AuthService.isAdmin();

    // Pide la lista de productos al backend
    const cargarProductos = useCallback(() => {
        ProductoService.getProductos()
            .then(response => setProductos(response.data))
            .catch(() => setMensaje('No se pudo cargar la lista de productos'));
    }, []);

    // Se ejecuta una vez al abrir la pantalla
    useEffect(() => {
        cargarProductos();
    }, [cargarProductos]);

    // Elimina un producto (pide confirmación) y vuelve a cargar la lista
    const eliminarProducto = (producto) => {
        if (!window.confirm(`¿Eliminar el producto "${producto.nombre}"?`)) {
            return;
        }
        setMensaje('');
        ProductoService.deleteProducto(producto.id)
            .then(() => cargarProductos())
            .catch((error) => {
                if (error.response && error.response.status === 403) {
                    setMensaje('No tienes permiso para eliminar productos');
                } else {
                    setMensaje('No se pudo eliminar el producto');
                }
            });
    };

    return (
        <div>
            <h2>Productos</h2>

            {/* Solo el ADMIN puede agregar productos */}
            {esAdmin && <Link to="/add">Agregar producto</Link>}

            {mensaje && <p style={{ color: 'red' }}>{mensaje}</p>}

            <table style={{ marginTop: '10px' }}>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Categoría</th>
                        <th>Marca</th>
                        <th>Precio</th>
                        <th>Stock</th>
                        {esAdmin && <th>Acciones</th>}
                    </tr>
                </thead>
                <tbody>
                    {productos.map(producto => (
                        <tr key={producto.id}>
                            <td>{producto.id}</td>
                            <td>{producto.nombre}</td>
                            <td>{producto.categoria}</td>
                            <td>{producto.marca}</td>
                            <td>${Number(producto.precio).toLocaleString('es-CL')}</td>
                            <td>{producto.stock}</td>
                            {esAdmin && (
                                <td>
                                    <Link to={`/edit/${producto.id}`}>Editar</Link>
                                    <button onClick={() => eliminarProducto(producto)} style={{ marginLeft: '10px' }}>
                                        Eliminar
                                    </button>
                                </td>
                            )}
                        </tr>
                    ))}
                </tbody>
            </table>

            {productos.length === 0 && !mensaje && <p>No hay productos registrados.</p>}
        </div>
    );
};

export default ProductoList;