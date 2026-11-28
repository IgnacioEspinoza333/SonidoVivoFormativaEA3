package sonidoVivo.tienda.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import sonidoVivo.tienda.entity.Usuario;
import sonidoVivo.tienda.repository.UsuarioRepository;

// Crea los usuarios de prueba al arrancar la aplicación (solo si no existen)
// Son los mismos usuarios que ya usaba el frontend de la tienda
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner crearUsuariosIniciales(UsuarioRepository usuarioRepository,
                                                    PasswordEncoder passwordEncoder) {
        return args -> {
            // Administrador: rol ADMIN
            Usuario admin = new Usuario("admin", null, "ADMIN");
            admin.setNombre("Administrador");
            admin.setApellido("Sonido Vivo");
            admin.setRut("11111111-1");
            admin.setEmail("admin@duocuc.cl");
            admin.setInstrumento("Guitarra");
            admin.setNivel("Profesional");
            guardarSiNoExiste(usuarioRepository, passwordEncoder, admin, "admin123");

            // Cliente: rol USER
            Usuario cliente = new Usuario("cliente", null, "USER");
            cliente.setNombre("Cliente");
            cliente.setApellido("Demo");
            cliente.setRut("22222222-2");
            cliente.setEmail("cliente@gmail.com");
            cliente.setInstrumento("Teclado / Piano");
            cliente.setNivel("Intermedio");
            guardarSiNoExiste(usuarioRepository, passwordEncoder, cliente, "cliente123");
        };
    }

    private void guardarSiNoExiste(UsuarioRepository repo, PasswordEncoder encoder,
                                   Usuario usuario, String passwordPlano) {
        if (repo.findByUsername(usuario.getUsername()).isEmpty()) {
            // La contraseña se encripta antes de guardarla
            usuario.setPassword(encoder.encode(passwordPlano));
            repo.save(usuario);
        }
    }
}