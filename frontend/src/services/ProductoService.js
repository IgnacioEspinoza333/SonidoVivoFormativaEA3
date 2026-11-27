import api from './api';

// Servicio de productos: una función por cada operación del CRUD
// Cada función devuelve la promesa de Axios; los datos vienen en response.data
const ProductoService = {

    // GET /api/productos
    getProductos: () => api.get('/productos'),

    // GET /api/productos/{id}
    getProductoById: (id) => api.get(`/productos/${id}`),

    // POST /api/productos
    createProducto: (producto) => api.post('/productos', producto),

    // PUT /api/productos/{id}
    updateProducto: (id, producto) => api.put(`/productos/${id}`, producto),

    // DELETE /api/productos/{id}
    deleteProducto: (id) => api.delete(`/productos/${id}`)
};

export default ProductoService;