package sonidoVivo.tienda.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sonidoVivo.tienda.entity.Producto;
import sonidoVivo.tienda.repository.ProductoRepository;

// Servicio de Producto: aquí va la lógica de negocio (el CRUD básico)
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Inyección por constructor: Spring nos entrega el repositorio ya creado
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // 1. Obtener todos los registros
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // 2. Buscar por ID (Optional puede venir vacío si el id no existe)
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    // 3. Guardar / crear un nuevo registro
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    // 4. Actualizar un registro existente
    // Si el id no existe, devuelve un Optional vacío
    public Optional<Producto> actualizar(Long id, Producto datos) {
        return productoRepository.findById(id).map(productoExistente -> {
            productoExistente.setNombre(datos.getNombre());
            productoExistente.setCategoria(datos.getCategoria());
            productoExistente.setMarca(datos.getMarca());
            productoExistente.setPrecio(datos.getPrecio());
            productoExistente.setStock(datos.getStock());
            productoExistente.setDescripcion(datos.getDescripcion());
            productoExistente.setImagen(datos.getImagen());
            return productoRepository.save(productoExistente);
        });
    }

    // 5. Eliminar por ID
    // Devuelve true si lo eliminó, false si el id no existía
    public boolean eliminar(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}