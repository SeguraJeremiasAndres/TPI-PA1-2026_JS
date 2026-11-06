package empleado.input;

import empleado.modelo.Puesto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CrearEmpleadoCommand(String nombreCompleto, String legajo,
                                   LocalDate fechaIngreso, Puesto puesto,
                                   BigDecimal salario) {
}