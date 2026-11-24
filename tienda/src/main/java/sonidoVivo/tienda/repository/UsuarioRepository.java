package sonidoVivo.tienda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sonidoVivo.tienda.entity.Usuario;

// Repositorio de Usuario: además del CRUD básico, sabe buscar por nombre de usuario
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring crea la consulta solo a partir del nombre del método
    Optional<Usuario> findByUsername(String username);
}