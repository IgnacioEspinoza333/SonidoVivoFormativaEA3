package sonidoVivo.tienda.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Datos que envía el formulario de registro, con las mismas reglas que el frontend
public record RegistroRequest(

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{3,}$",
                 message = "Ingresa un nombre válido (solo letras, mínimo 3)")
        String nombre,

        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{3,}$",
                 message = "Ingresa un apellido válido (solo letras, mínimo 3)")
        String apellido,

        @Pattern(regexp = "^[0-9]{7,8}-[0-9Kk]$",
                 message = "Formato incorrecto. Ej: 12345678-9")
        String rut,

        @Pattern(regexp = "^[a-zA-Z0-9_.-]{4,}$",
                 message = "El usuario debe tener al menos 4 caracteres (sin espacios)")
        String usuario,

        @Pattern(regexp = "^[\\w.+-]+@(gmail\\.com|outlook\\.com|duocuc\\.cl|hotmail\\.com)$",
                 flags = Pattern.Flag.CASE_INSENSITIVE,
                 message = "Ingresa un correo válido (gmail, outlook, duocuc, hotmail)")
        String email,

        @NotNull(message = "Ingresa tu fecha de nacimiento")
        @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
        LocalDate fechaNacimiento,

        @NotBlank(message = "Selecciona tu instrumento principal")
        String instrumento,

        @NotBlank(message = "Selecciona tu nivel de experiencia")
        String nivel,

        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password) {
}