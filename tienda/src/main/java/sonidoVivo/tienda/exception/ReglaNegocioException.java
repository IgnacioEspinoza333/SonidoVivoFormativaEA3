package sonidoVivo.tienda.exception;

// Se lanza cuando una operación incumple una regla del negocio (ej: no hay stock suficiente)
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}