package sonidoVivo.tienda.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import sonidoVivo.tienda.dto.CompraRequest;
import sonidoVivo.tienda.dto.CompraResponse;
import sonidoVivo.tienda.entity.Venta;
import sonidoVivo.tienda.service.VentaService;

@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "Registro y consulta de ventas realizadas en la tienda")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // Compra desde el carrito: valida stock, calcula el total y descuenta el stock
    // El comprador se toma del token (authentication), no del cuerpo de la petición
    @PostMapping("/comprar")
    @Operation(summary = "Registrar una compra",
               description = "Recibe el carrito (productoId y cantidad de cada línea) y los datos de pago y entrega. "
                           + "Valida que existan los productos y que haya stock, calcula el total y descuenta el stock. "
                           + "Si algo falla, no se guarda nada.")
    public ResponseEntity<CompraResponse> comprar(@Valid @RequestBody CompraRequest compra,
                                                  Authentication authentication) {
        CompraResponse respuesta = ventaService.registrarCompra(authentication.getName(), compra);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping
    @Operation(summary = "Listar todas las ventas", description = "Obtiene el historial completo de ventas registradas")
    public List<Venta> obtenerTodos() {
        return ventaService.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar venta por ID", description = "Devuelve el comprobante de una venta específica según su ID")
    public ResponseEntity<Venta> buscarPorId(@PathVariable Long id) {
        return ventaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // @Valid hace que Spring revise las validaciones de la entidad antes de guardar
    @PostMapping
    @Operation(summary = "Registrar nueva venta", description = "Guarda una venta asociando clienteId, productoId, cantidad y total en CLP")
    public ResponseEntity<Venta> guardar(@Valid @RequestBody Venta venta) {
        Venta nueva = ventaService.guardar(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar venta", description = "Modifica los datos de una venta existente según su ID")
    public ResponseEntity<Venta> actualizar(@PathVariable Long id, @Valid @RequestBody Venta venta) {
        return ventaService.actualizar(id, venta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar venta", description = "Elimina el registro de una venta según su ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (ventaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}