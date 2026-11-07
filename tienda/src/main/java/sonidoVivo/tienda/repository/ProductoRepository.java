package sonidoVivo.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sonidoVivo.tienda.entity.Producto;

// Repositorio de Producto: JpaRepository ya trae guardar, buscar, listar y eliminar
// <Producto, Long> = entidad que maneja y tipo de dato de su id
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}