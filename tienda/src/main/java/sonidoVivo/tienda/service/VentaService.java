package sonidoVivo.tienda.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sonidoVivo.tienda.dto.CompraRequest;
import sonidoVivo.tienda.dto.CompraResponse;
import sonidoVivo.tienda.entity.Producto;
import sonidoVivo.tienda.entity.Usuario;
import sonidoVivo.tienda.entity.Venta;
import sonidoVivo.tienda.exception.ReglaNegocioException;
import sonidoVivo.tienda.repository.ProductoRepository;
import sonidoVivo.tienda.repository.UsuarioRepository;
import sonidoVivo.tienda.repository.VentaRepository;

// Servicio de Venta: el CRUD básico y la lógica de negocio de la compra
@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    // Inyección por constructor: Spring nos entrega los repositorios ya creados
    public VentaService(VentaRepository ventaRepository,
                        ProductoRepository productoRepository,
                        UsuarioRepository usuarioRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
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
            ventaExistente.setRegion(datos.getRegion());
            ventaExistente.setDireccion(datos.getDireccion());
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

    // 6. LÓGICA DE NEGOCIO: registrar una compra completa
    // @Transactional: si algo falla a la mitad, se deshace TODO (no queda stock descontado a medias)
    @Transactional
    public CompraResponse registrarCompra(String username, CompraRequest compra) {

        // El comprador es el usuario del token, no un dato que envíe el frontend
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ReglaNegocioException("El usuario no existe"));

        // Regla: para el despacho a domicilio se necesita región y dirección
        boolean despacho = "despacho".equals(compra.tipoEntrega());
        if (despacho && (estaVacio(compra.region()) || estaVacio(compra.direccion()))) {
            throw new ReglaNegocioException("Para el despacho debes indicar región y dirección");
        }

        List<Venta> ventas = new ArrayList<>();
        int totalCompra = 0;
        int unidades = 0;

        for (CompraRequest.Item item : compra.items()) {

            // Regla: el producto debe existir
            Producto producto = productoRepository.findById(item.productoId())
                    .orElseThrow(() -> new ReglaNegocioException(
                            "El producto " + item.productoId() + " no existe"));

            // Regla: debe haber stock suficiente
            if (producto.getStock() < item.cantidad()) {
                throw new ReglaNegocioException("No hay stock suficiente de \"" + producto.getNombre()
                        + "\" (disponible: " + producto.getStock() + ")");
            }

            // Se descuenta el stock
            producto.setStock(producto.getStock() - item.cantidad());
            productoRepository.save(producto);

            // Se registra la venta; el total lo calcula el backend (precio x cantidad)
            Venta venta = new Venta();
            venta.setClienteId(usuario.getId());
            venta.setProductoId(producto.getId());
            venta.setCantidad(item.cantidad());
            venta.setTotal(producto.getPrecio() * item.cantidad());
            venta.setFormaPago(compra.formaPago());
            venta.setTipoEntrega(compra.tipoEntrega());
            if (despacho) {
                venta.setRegion(compra.region().trim());
                venta.setDireccion(compra.direccion().trim());
            }
            ventas.add(ventaRepository.save(venta));

            totalCompra += venta.getTotal();
            unidades += item.cantidad();
        }

        return new CompraResponse(unidades, totalCompra, ventas);
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}