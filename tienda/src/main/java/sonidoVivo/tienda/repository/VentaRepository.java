package sonidoVivo.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sonidoVivo.tienda.entity.Venta;

// Repositorio de Venta: JpaRepository ya trae guardar, buscar, listar y eliminar
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
}