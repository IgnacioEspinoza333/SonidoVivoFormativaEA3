package sonidoVivo.tienda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sonidoVivo.tienda.entity.Usuario;

// Repositorio de Usuario: además del CRUD básico, sabe buscar y comprobar si ya existe un dato
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring crea cada consulta solo a partir del nombre del método
    Optional<Usuario> findByUsername(String username);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByRut(String rut);
}