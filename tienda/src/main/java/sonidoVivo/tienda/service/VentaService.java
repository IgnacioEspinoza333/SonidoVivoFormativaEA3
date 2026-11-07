package sonidoVivo.tienda.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sonidoVivo.tienda.entity.Venta;
import sonidoVivo.tienda.repository.VentaRepository;

// Servicio de Venta: aquí va la lógica de negocio (el CRUD básico)
@Service
public class VentaService {

    private final VentaRepository ventaRepository;

    // Inyección por constructor: Spring nos entrega el repositorio ya creado
    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    // 1. Obtener todos los registros
    public List<Venta> obtenerTodos() {
        return ventaRepository.findAll();
    }

    // 2. Buscar por ID (Optional puede venir vacío si el id no existe)
    public Optional<Venta> buscarPorId(Long id) {
        return ventaRepository.findById(id);
    }

    // 3. Guardar / crear un nuevo registro
    public Venta guardar(Venta venta) {
        return ventaRepository.save(venta);
    }

    // 4. Actualizar un registro existente
    // Si el id no existe, devuelve un Optional vacío
    public Optional<Venta> actualizar(Long id, Venta datos) {
        return ventaRepository.findById(id).map(ventaExistente -> {
            ventaExistente.setClienteId(datos.getClienteId());
            ventaExistente.setProductoId(datos.getProductoId());
            ventaExistente.setCantidad(datos.getCantidad());
            ventaExistente.setTotal(datos.getTotal());
            ventaExistente.setFormaPago(datos.getFormaPago());
            ventaExistente.setTipoEntrega(datos.getTipoEntrega());
            // La fecha solo se cambia si viene en los datos; si no, se conserva la original
            if (datos.getFecha() != null) {
                ventaExistente.setFecha(datos.getFecha());
            }
            return ventaRepository.save(ventaExistente);
        });
    }

    // 5. Eliminar por ID
    // Devuelve true si lo eliminó, false si el id no existía
    public boolean eliminar(Long id) {
        if (ventaRepository.existsById(id)) {
            ventaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}