package empleado.modelo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EmpleadoTest {

    @Test
    void instance_conDatosValidos_creaEmpleadoConSusDatos() {
        LocalDate fechaIngreso = LocalDate.of(2024, 3, 15);

        Empleado empleado = Empleado.instance(
                "Juan Perez",
                "ABC1234",
                fechaIngreso,
                Puesto.ANALISTA,
                new BigDecimal("600000"));

        assertNotNull(empleado.getId());
        assertEquals("Juan Perez", empleado.getNombreCompleto());
        assertEquals("ABC1234", empleado.getLegajo());
        assertEquals(fechaIngreso, empleado.getFechaIngreso());
        assertEquals(Puesto.ANALISTA, empleado.getPuesto());
        assertEquals(new BigDecimal("600000"), empleado.getSalario());
    }
}