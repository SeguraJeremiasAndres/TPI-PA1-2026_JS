package empleado.modelo;

import empleado.exception.CampoObligatorioException;
import empleado.exception.LegajoInvalidoException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

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
    @Test
    void instance_conNombreCompletoNulo_lanzaCampoObligatorioException() {
        CampoObligatorioException ex = assertThrows(CampoObligatorioException.class, () ->
                Empleado.instance(null, "ABC1234", LocalDate.of(2024, 3, 15),
                        Puesto.ANALISTA, new BigDecimal("600000")));

        assertTrue(ex.getMessage().contains("nombreCompleto"));
    }

    @Test
    void instance_conLegajoNulo_lanzaCampoObligatorioException() {
        CampoObligatorioException ex = assertThrows(CampoObligatorioException.class, () ->
                Empleado.instance("Juan Perez", null, LocalDate.of(2024, 3, 15),
                        Puesto.ANALISTA, new BigDecimal("600000")));

        assertTrue(ex.getMessage().contains("legajo"));
    }

    @Test
    void instance_conFechaIngresoNula_lanzaCampoObligatorioException() {
        CampoObligatorioException ex = assertThrows(CampoObligatorioException.class, () ->
                Empleado.instance("Juan Perez", "ABC1234", null,
                        Puesto.ANALISTA, new BigDecimal("600000")));

        assertTrue(ex.getMessage().contains("fechaIngreso"));
    }

    @Test
    void instance_conPuestoNulo_lanzaCampoObligatorioException() {
        CampoObligatorioException ex = assertThrows(CampoObligatorioException.class, () ->
                Empleado.instance("Juan Perez", "ABC1234", LocalDate.of(2024, 3, 15),
                        null, new BigDecimal("600000")));

        assertTrue(ex.getMessage().contains("puesto"));
    }

    @Test
    void instance_conSalarioNulo_lanzaCampoObligatorioException() {
        CampoObligatorioException ex = assertThrows(CampoObligatorioException.class, () ->
                Empleado.instance("Juan Perez", "ABC1234", LocalDate.of(2024, 3, 15),
                        Puesto.ANALISTA, null));

        assertTrue(ex.getMessage().contains("salario"));
    }
    @Test
    void instance_conLegajoEnMinusculas_lanzaLegajoInvalidoException() {
        LegajoInvalidoException ex = assertThrows(LegajoInvalidoException.class, () ->
                Empleado.instance("Juan Perez", "abc1234", LocalDate.of(2024, 3, 15),
                        Puesto.ANALISTA, new BigDecimal("600000")));

        assertTrue(ex.getMessage().contains("legajo"));
    }

    @Test
    void instance_conLegajoConMenosDeCuatroNumeros_lanzaLegajoInvalidoException() {
        LegajoInvalidoException ex = assertThrows(LegajoInvalidoException.class, () ->
                Empleado.instance("Juan Perez", "ABC123", LocalDate.of(2024, 3, 15),
                        Puesto.ANALISTA, new BigDecimal("600000")));

        assertTrue(ex.getMessage().contains("legajo"));
    }

    @Test
    void instance_conLegajoConMasDeCuatroNumeros_lanzaLegajoInvalidoException() {
        LegajoInvalidoException ex = assertThrows(LegajoInvalidoException.class, () ->
                Empleado.instance("Juan Perez", "ABC12345", LocalDate.of(2024, 3, 15),
                        Puesto.ANALISTA, new BigDecimal("600000")));

        assertTrue(ex.getMessage().contains("legajo"));
    }

    @Test
    void instance_conLegajoConLetrasEnLugarDeNumeros_lanzaLegajoInvalidoException() {
        LegajoInvalidoException ex = assertThrows(LegajoInvalidoException.class, () ->
                Empleado.instance("Juan Perez", "ABCDEFG", LocalDate.of(2024, 3, 15),
                        Puesto.ANALISTA, new BigDecimal("600000")));

        assertTrue(ex.getMessage().contains("legajo"));
    }

    @Test
    void instance_conLegajoConNumerosAntesDeLasLetras_lanzaLegajoInvalidoException() {
        LegajoInvalidoException ex = assertThrows(LegajoInvalidoException.class, () ->
                Empleado.instance("Juan Perez", "1234ABC", LocalDate.of(2024, 3, 15),
                        Puesto.ANALISTA, new BigDecimal("600000")));

        assertTrue(ex.getMessage().contains("legajo"));
    }
}