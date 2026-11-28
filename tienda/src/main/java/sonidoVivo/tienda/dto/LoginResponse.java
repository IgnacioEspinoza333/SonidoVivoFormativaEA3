package sonidoVivo.tienda.dto;

// Respuesta del login: el token JWT, su tipo, el rol y los datos del usuario
public record LoginResponse(String token, String tipo, String rol, UsuarioResponse usuario) {
}