package sonidoVivo.tienda.controller;

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
import sonidoVivo.tienda.entity.Usuario;
import sonidoVivo.tienda.repository.UsuarioRepository;
import sonidoVivo.tienda.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para iniciar sesión y obtener token JWT")
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
               description = "Valida usuario y contraseña contra la base de datos y devuelve un token JWT. "
                           + "Usuarios de prueba: admin / admin1234 y user / user1234")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String username = credenciales.get("username");
        String password = credenciales.get("password");

        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas"));
        }

        // 1. Buscamos al usuario en la base de datos
        Optional<Usuario> encontrado = usuarioRepository.findByUsername(username);

        // 2. Comparamos la contraseña escrita con la encriptada que está guardada
        if (encontrado.isPresent() && passwordEncoder.matches(password, encontrado.get().getPassword())) {
            Usuario usuario = encontrado.get();
            // 3. El token ahora lleva dentro el rol del usuario
            String token = jwtUtil.generarToken(usuario.getUsername(), usuario.getRol());
            return ResponseEntity.ok(Map.of(
                "token", token,
                "tipo", "Bearer",
                "rol", usuario.getRol()
            ));
        }

        // Mismo mensaje si falla el usuario o la contraseña, para no dar pistas
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenciales inválidas"));
    }
}