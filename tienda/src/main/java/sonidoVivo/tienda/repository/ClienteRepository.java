package sonidoVivo.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sonidoVivo.tienda.entity.Cliente;

// Repositorio de Cliente: JpaRepository ya trae guardar, buscar, listar y eliminar
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}