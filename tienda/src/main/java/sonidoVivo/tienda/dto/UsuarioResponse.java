package sonidoVivo.tienda.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import sonidoVivo.tienda.entity.Usuario;

// Datos públicos del usuario (sin contraseña). Las claves son las que usa el frontend
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UsuarioResponse(
        String nombre,
        String apellido,
        String rut,
        String usuario,
        String email,
        LocalDate fechaNacimiento,
        String instrumento,
        String nivel,
        String rol) {

    // Convierte la entidad Usuario (de la base de datos) en esta respuesta
    public static UsuarioResponse desde(Usuario u) {
        return new UsuarioResponse(
                u.getNombre(),
                u.getApellido(),
                u.getRut(),
                u.getUsername(),
                u.getEmail(),
                u.getFechaNacimiento(),
                u.getInstrumento(),
                u.getNivel(),
                u.getRol());
    }
}