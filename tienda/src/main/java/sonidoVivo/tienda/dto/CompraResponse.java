package sonidoVivo.tienda.dto;

import java.util.List;

import sonidoVivo.tienda.entity.Venta;

// Resumen de la compra realizada: unidades, total en CLP y las ventas registradas
public record CompraResponse(int unidades, int total, List<Venta> ventas) {
}