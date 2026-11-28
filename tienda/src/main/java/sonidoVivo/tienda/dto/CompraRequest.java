package sonidoVivo.tienda.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// Datos que envía el checkout: cómo paga, cómo recibe y qué productos lleva
public record CompraRequest(

        @NotBlank(message = "Selecciona la forma de pago")
        @Pattern(regexp = "debito|credito", message = "La forma de pago debe ser debito o credito")
        String formaPago,

        @NotBlank(message = "Selecciona el tipo de entrega")
        @Pattern(regexp = "retiro|despacho", message = "El tipo de entrega debe ser retiro o despacho")
        String tipoEntrega,

        // Solo se exigen cuando el tipo de entrega es "despacho"
        String region,
        String direccion,

        @NotEmpty(message = "El carrito no puede estar vacío")
        @Valid
        List<Item> items) {

    // Cada línea del carrito: qué producto y cuántas unidades
    public record Item(
            @NotNull(message = "El productoId es obligatorio")
            Long productoId,

            @NotNull(message = "La cantidad es obligatoria")
            @Min(value = 1, message = "La cantidad debe ser al menos 1")
            Integer cantidad) {
    }
}