package ar.edu.undec.adapter.handler;

import empleado.exception.CampoObligatorioException;
import empleado.exception.EmpleadoNoEncontradoException;
import empleado.exception.FechaIngresoInvalidaException;
import empleado.exception.LegajoDuplicadoException;
import empleado.exception.LegajoInvalidoException;
import empleado.exception.PersistenciaException;
import empleado.exception.SalarioInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import empleado.exception.NombreInvalidoException;
import empleado.modelo.Puesto;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CampoObligatorioException.class,
            LegajoInvalidoException.class,
            FechaIngresoInvalidaException.class,
            SalarioInvalidoException.class,
            NombreInvalidoException.class})
    public ResponseEntity<ErrorResponse> manejarReglaDeNegocioViolada(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(EmpleadoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarEmpleadoNoEncontrado(EmpleadoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(LegajoDuplicadoException.class)
    public ResponseEntity<ErrorResponse> manejarLegajoDuplicado(LegajoDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(PersistenciaException.class)
    public ResponseEntity<ErrorResponse> manejarErrorDePersistencia(PersistenciaException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ex.getMessage()));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarSolicitudIlegible() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "El cuerpo de la solicitud no es valido. Revisar que sea un JSON bien formado, "
                                + "que la fecha use el formato aaaa-mm-dd y que el puesto sea uno de "
                                + Arrays.toString(Puesto.values())));
    }
}