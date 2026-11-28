import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ProductoService from '../services/ProductoService';

const ProductoForm = () => {
    // Todos los campos del producto en un solo objeto
    const [producto, setProducto] = useState({
        nombre: '',
        categoria: '',
        marca: '',
        precio: '',
        stock: '',
        descripcion: '',
        imagen: ''
    });
    const [errores, setErrores] = useState([]);
    const { id } = useParams();      // si hay id en la dirección, estamos editando
    const navigate = useNavigate();

    // Al editar, carga los datos actuales del producto
    useEffect(() => {
        if (id) {
            ProductoService.getProductoById(id)
                .then(response => {
                    const datos = response.data;
                    setProducto({
                        nombre: datos.nombre || '',
                        categoria: datos.categoria || '',
                        marca: datos.marca || '',
                        precio: datos.precio,
                        stock: datos.stock,
                        descripcion: datos.descripcion || '',
                        imagen: datos.imagen || ''
                    });
                })
                .catch(() => setErrores(['No se encontró el producto']));
        }
    }, [id]);

    // Un solo manejador para todos los campos: usa el "name" de cada input
    const handleChange = (e) => {
        const { name, value } = e.target;
        setProducto({ ...producto, [name]: value });
    };

    const guardarProducto = (e) => {
        e.preventDefault();
        setErrores([]);

        // precio y stock se envían como números enteros
        const datos = {
            ...producto,
            precio: parseInt(producto.precio, 10),
            stock: parseInt(producto.stock, 10)
        };

        const peticion = id
            ? ProductoService.updateProducto(id, datos)
            : ProductoService.createProducto(datos);

        peticion
            .then(() => navigate('/'))
            .catch((error) => {
                const respuesta = error.response;
                if (respuesta && respuesta.status === 400 && respuesta.data.detalles) {
                    // Errores de validación del backend: uno por campo
                    setErrores(Object.values(respuesta.data.detalles));
                } else if (respuesta && respuesta.status === 403) {
                    setErrores(['No tienes permiso para realizar esta acción']);
                } else if (respuesta && respuesta.status === 404) {
                    setErrores(['El producto no existe']);
                } else {
                    setErrores(['No se pudo guardar el producto']);
                }
            });
    };

    return (
        <div>
            <h2>{id ? 'Editar producto' : 'Agregar producto'}</h2>

            {errores.length > 0 && (
                <ul style={{ color: 'red' }}>
                    {errores.map((mensaje, i) => <li key={i}>{mensaje}</li>)}
                </ul>
            )}

            <form onSubmit={guardarProducto}>
                <div>
                    <label>Nombre: </label>
                    <input type="text" name="nombre" value={producto.nombre} onChange={handleChange} required />
                </div>
                <div style={{ marginTop: '10px' }}>
                    <label>Categoría: </label>
                    <input type="text" name="categoria" value={producto.categoria} onChange={handleChange} />
                </div>
                <div style={{ marginTop: '10px' }}>
                    <label>Marca: </label>
                    <input type="text" name="marca" value={producto.marca} onChange={handleChange} />
                </div>
                <div style={{ marginTop: '10px' }}>
                    <label>Precio (CLP): </label>
                    <input type="number" name="precio" min="1" value={producto.precio} onChange={handleChange} required />
                </div>
                <div style={{ marginTop: '10px' }}>
                    <label>Stock: </label>
                    <input type="number" name="stock" min="0" value={producto.stock} onChange={handleChange} required />
                </div>
                <div style={{ marginTop: '10px' }}>
                    <label>Descripción: </label>
                    <input type="text" name="descripcion" value={producto.descripcion} onChange={handleChange} />
                </div>
                <div style={{ marginTop: '10px' }}>
                    <label>Imagen (nombre del archivo): </label>
                    <input type="text" name="imagen" value={producto.imagen} onChange={handleChange} />
                </div>
                <button type="submit" style={{ marginTop: '10px' }}>{id ? 'Actualizar' : 'Guardar'}</button>
                <button type="button" onClick={() => navigate('/')} style={{ marginTop: '10px', marginLeft: '10px' }}>
                    Cancelar
                </button>
            </form>
        </div>
    );
};

export default ProductoForm;