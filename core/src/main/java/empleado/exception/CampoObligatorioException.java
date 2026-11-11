package empleado.exception;

public class CampoObligatorioException extends RuntimeException {
    public CampoObligatorioException(String message) {
        super(message);
    }
}
