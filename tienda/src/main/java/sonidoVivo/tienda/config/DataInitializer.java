package sonidoVivo.tienda.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import sonidoVivo.tienda.entity.Usuario;
import sonidoVivo.tienda.repository.UsuarioRepository;

// Crea los usuarios de prueba al arrancar la aplicación (solo si no existen)
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner crearUsuariosIniciales(UsuarioRepository usuarioRepository,
                                                    PasswordEncoder passwordEncoder) {
        return args -> {
            crearSiNoExiste(usuarioRepository, passwordEncoder, "admin", "admin1234", "ADMIN");
            crearSiNoExiste(usuarioRepository, passwordEncoder, "user", "user1234", "USER");
        };
    }

    private void crearSiNoExiste(UsuarioRepository repo, PasswordEncoder encoder,
                                 String username, String passwordPlano, String rol) {
        if (repo.findByUsername(username).isEmpty()) {
            // La contraseña se encripta antes de guardarla
            repo.save(new Usuario(username, encoder.encode(passwordPlano), rol));
        }
    }
}