package sonidoVivo.tienda.dto;

// Datos que envía el formulario de login: usuario, correo y contraseña
public record LoginRequest(String usuario, String email, String password) {
}