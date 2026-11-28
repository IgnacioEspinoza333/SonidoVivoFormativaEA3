package sonidoVivo.tienda.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import sonidoVivo.tienda.dto.LoginRequest;
import sonidoVivo.tienda.dto.LoginResponse;
import sonidoVivo.tienda.dto.RegistroRequest;
import sonidoVivo.tienda.dto.UsuarioResponse;
import sonidoVivo.tienda.entity.Usuario;
import sonidoVivo.tienda.repository.UsuarioRepository;
import sonidoVivo.tienda.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para registrarse, iniciar sesión y obtener token JWT")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtUtil jwtUtil, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión",
               description = "Valida usuario, correo y contraseña contra la base de datos y devuelve un token JWT "
                           + "junto con los datos del usuario. Usuarios de prueba: admin / admin@duocuc.cl / admin123 "
                           + "y cliente / cliente@gmail.com / cliente123")
    public ResponseEntity<?> login(@RequestBody LoginRequest datos) {
        if (datos.usuario() != null && datos.email() != null && datos.password() != null) {

            // 1. Buscamos al usuario en la base de datos
            Optional<Usuario> encontrado = usuarioRepository.findByUsername(datos.usuario().trim());

            if (encontrado.isPresent()) {
                Usuario usuario = encontrado.get();

                // 2. El correo debe coincidir y la contraseña escrita debe coincidir con la encriptada
                boolean correoCorrecto = usuario.getEmail() != null
                        && usuario.getEmail().equalsIgnoreCase(datos.email().trim());
                boolean claveCorrecta = passwordEncoder.matches(datos.password().trim(), usuario.getPassword());

                if (correoCorrecto && claveCorrecta) {
                    // 3. El token lleva dentro el rol del usuario
                    String token = jwtUtil.generarToken(usuario.getUsername(), usuario.getRol());
                    return ResponseEntity.ok(
                            new LoginResponse(token, "Bearer", usuario.getRol(), UsuarioResponse.desde(usuario)));
                }
            }
        }

        // Mismo mensaje en todos los casos de error, para no dar pistas
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Usuario, correo o contraseña incorrectos."));
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario",
               description = "Crea una cuenta nueva con rol USER. El usuario, el correo y el RUT no pueden repetirse")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequest datos) {

        // Regla del frontend: solo mayores de 18 años
        if (Period.between(datos.fechaNacimiento(), LocalDate.now()).getYears() < 18) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Debes ser mayor de 18 años"));
        }

        // Datos que no se pueden repetir
        if (usuarioRepository.existsByUsernameIgnoreCase(datos.usuario().trim())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ese nombre de usuario ya está en uso."));
        }
        if (usuarioRepository.existsByEmailIgnoreCase(datos.email().trim())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ese correo ya está registrado."));
        }
        if (usuarioRepository.existsByRut(datos.rut().trim())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ese RUT ya está registrado."));
        }

        // Todo registro nuevo es USER; la contraseña se guarda encriptada
        Usuario nuevo = new Usuario(datos.usuario().trim(), passwordEncoder.encode(datos.password().trim()), "USER");
        nuevo.setNombre(datos.nombre().trim());
        nuevo.setApellido(datos.apellido().trim());
        nuevo.setRut(datos.rut().trim());
        nuevo.setEmail(datos.email().trim().toLowerCase());
        nuevo.setFechaNacimiento(datos.fechaNacimiento());
        nuevo.setInstrumento(datos.instrumento());
        nuevo.setNivel(datos.nivel());

        usuarioRepository.save(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.desde(nuevo));
    }
}