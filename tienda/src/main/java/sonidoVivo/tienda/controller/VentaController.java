package sonidoVivo.tienda.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sonidoVivo.tienda.entity.Venta;
import sonidoVivo.tienda.service.VentaService;

// Controlador REST de Venta: recibe las peticiones HTTP y responde en JSON
// Todas las rutas empiezan con /api/ventas
@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    // Inyección por constructor: Spring nos entrega el servicio ya creado
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // GET /api/ventas -> lista todas las ventas
    @GetMapping
    public List<Venta> obtenerTodos() {
        return ventaService.obtenerTodos();
    }

    // GET /api/ventas/{id} -> busca una venta por id
    // Si no existe responde 404 (no encontrado)
    @GetMapping("/{id}")
    public ResponseEntity<Venta> buscarPorId(@PathVariable Long id) {
        return ventaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/ventas -> crea una venta nueva (responde 201 creado)
    @PostMapping
    public ResponseEntity<Venta> guardar(@RequestBody Venta venta) {
        Venta nueva = ventaService.guardar(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // PUT /api/ventas/{id} -> actualiza una venta existente
    // Si no existe responde 404 (no encontrado)
    @PutMapping("/{id}")
    public ResponseEntity<Venta> actualizar(@PathVariable Long id, @RequestBody Venta venta) {
        return ventaService.actualizar(id, venta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/ventas/{id} -> elimina una venta
    // Responde 204 (sin contenido) si la eliminó, o 404 si no existía
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (ventaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}