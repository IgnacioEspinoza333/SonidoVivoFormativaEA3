package sonidoVivo.tienda.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sonidoVivo.tienda.entity.Cliente;
import sonidoVivo.tienda.repository.ClienteRepository;

// Servicio de Cliente: aquí va la lógica de negocio (el CRUD básico)
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    // Inyección por constructor: Spring nos entrega el repositorio ya creado
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // 1. Obtener todos los registros
    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    // 2. Buscar por ID (Optional puede venir vacío si el id no existe)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    // 3. Guardar / crear un nuevo registro
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // 4. Actualizar un registro existente
    // Si el id no existe, devuelve un Optional vacío
    public Optional<Cliente> actualizar(Long id, Cliente datos) {
        return clienteRepository.findById(id).map(clienteExistente -> {
            clienteExistente.setNombre(datos.getNombre());
            clienteExistente.setApellido(datos.getApellido());
            clienteExistente.setRut(datos.getRut());
            clienteExistente.setEmail(datos.getEmail());
            clienteExistente.setTelefono(datos.getTelefono());
            clienteExistente.setDireccion(datos.getDireccion());
            return clienteRepository.save(clienteExistente);
        });
    }

    // 5. Eliminar por ID
    // Devuelve true si lo eliminó, false si el id no existía
    public boolean eliminar(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}