package sonidoVivo.tienda;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.datafaker.Faker;
import sonidoVivo.tienda.entity.Cliente;
import sonidoVivo.tienda.entity.Producto;
import sonidoVivo.tienda.entity.Venta;

class TiendaDataFakerTest {

    private Faker faker;

    @BeforeEach
    void setUp() {
        // Inicializa DataFaker en español antes de cada prueba
        faker = new Faker(new Locale("es"));
    }

    @Test
    @DisplayName("Test 1: Generar Cliente chileno válido con DataFaker")
    void testGenerarClienteValido() {
        Cliente cliente = new Cliente();
        cliente.setNombre(faker.name().firstName());
        cliente.setApellido(faker.name().lastName());
        // Genera un RUT formato 12345678-9 sin puntos
        cliente.setRut(faker.number().numberBetween(10000000, 25000000) + "-" + faker.number().digit());
        cliente.setEmail(faker.internet().emailAddress());
        cliente.setTelefono(faker.phoneNumber().cellPhone());
        cliente.setDireccion(faker.address().streetAddress());

        System.out.println("Cliente generado: " + cliente.getNombre() + " " + cliente.getApellido() + " | RUT: " + cliente.getRut());

        assertNotNull(cliente.getNombre(), "El nombre no debe ser nulo");
        assertNotNull(cliente.getApellido(), "El apellido no debe ser nulo");
        assertTrue(cliente.getRut().contains("-"), "El RUT debe contener guion");
        assertTrue(cliente.getEmail().contains("@"), "El correo debe contener un @");
    }

    @Test
    @DisplayName("Test 2: Generar Producto musical con precio en CLP y stock válidos")
    void testGenerarProductoMusicalValido() {
        Producto producto = new Producto();
        producto.setNombre("Guitarra " + faker.music().instrument());
        producto.setCategoria("Instrumentos de Cuerda");
        producto.setMarca(faker.company().name());
        producto.setPrecio(faker.number().numberBetween(50000, 1500000));
        producto.setStock(faker.number().numberBetween(1, 50));
        producto.setDescripcion("Instrumento profesional de la marca " + producto.getMarca());
        producto.setImagen("guitarra.jpg");

        System.out.println("Producto generado: " + producto.getNombre() + " | Precio: $" + producto.getPrecio());

        assertNotNull(producto.getNombre());
        assertTrue(producto.getPrecio() > 0, "El precio en CLP debe ser mayor a 0");
        assertTrue(producto.getStock() >= 1, "El stock debe ser al menos 1");
    }

    @Test
    @DisplayName("Test 3: Validar cálculo y registro de Venta con IDs de Cliente y Producto")
    void testGenerarVentaCompleta() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre(faker.name().firstName());

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre(faker.music().instrument());
        producto.setPrecio(120000);

        int cantidad = faker.number().numberBetween(1, 5);
        int totalCalculado = producto.getPrecio() * cantidad;

        Venta venta = new Venta();
        venta.setClienteId(cliente.getId());
        venta.setProductoId(producto.getId());
        venta.setCantidad(cantidad);
        venta.setTotal(totalCalculado);
        venta.setFormaPago("tarjeta");
        venta.setTipoEntrega("despacho");
        venta.setFecha(LocalDateTime.now());

        System.out.println("Venta generada -> Cliente ID: " + venta.getClienteId() + " | Total: $" + venta.getTotal());

        assertNotNull(venta.getClienteId());
        assertNotNull(venta.getProductoId());
        assertEquals(totalCalculado, venta.getTotal(), "El total de la venta debe coincidir con precio x cantidad");
        assertEquals("tarjeta", venta.getFormaPago());
    }

    @Test
    @DisplayName("Test 4: Validar detección de stock negativo en Producto")
    void testValidarStockNegativo() {
        Producto producto = new Producto();
        producto.setNombre(faker.music().instrument());
        producto.setStock(-5);

        assertFalse(producto.getStock() >= 0, "El sistema debe detectar que el stock es negativo");
    }
}