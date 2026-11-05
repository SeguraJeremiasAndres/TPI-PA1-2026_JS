package empleado.modelo;

import empleado.exception.CampoObligatorioException;
import empleado.exception.FechaIngresoInvalidaException;
import empleado.exception.LegajoInvalidoException;
import empleado.exception.SalarioInvalidoException;
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
    @Test
    void instance_conFechaIngresoPosteriorAHoy_lanzaFechaIngresoInvalidaException() {
        FechaIngresoInvalidaException ex = assertThrows(FechaIngresoInvalidaException.class, () ->
                Empleado.instance("Juan Perez", "ABC1234", LocalDate.now().plusDays(1),
                        Puesto.ANALISTA, new BigDecimal("600000")));

        assertTrue(ex.getMessage().contains("fechaIngreso"));
    }

    @Test
    void instance_conFechaIngresoDeHoy_creaEmpleado() {
        LocalDate hoy = LocalDate.now();

        Empleado empleado = Empleado.instance("Juan Perez", "ABC1234", hoy,
                Puesto.ANALISTA, new BigDecimal("600000"));

        assertEquals(hoy, empleado.getFechaIngreso());
    }
    @Test
    void instance_conSalarioCero_lanzaSalarioInvalidoException() {
        SalarioInvalidoException ex = assertThrows(SalarioInvalidoException.class, () ->
                Empleado.instance("Juan Perez", "ABC1234", LocalDate.of(2024, 3, 15),
                        Puesto.ANALISTA, BigDecimal.ZERO));

        assertTrue(ex.getMessage().contains("salario"));
    }

    @Test
    void instance_conSalarioNegativo_lanzaSalarioInvalidoException() {
        SalarioInvalidoException ex = assertThrows(SalarioInvalidoException.class, () ->
                Empleado.instance("Juan Perez", "ABC1234", LocalDate.of(2024, 3, 15),
                        Puesto.ANALISTA, new BigDecimal("-1")));

        assertTrue(ex.getMessage().contains("salario"));
    }

    @Test
    void instance_conAnalistaPorDebajoDelPiso_lanzaSalarioInvalidoException() {
        SalarioInvalidoException ex = assertThrows(SalarioInvalidoException.class, () ->
                Empleado.instance("Juan Perez", "ABC1234", LocalDate.of(2024, 3, 15),
                        Puesto.ANALISTA, new BigDecimal("499999.99")));

        assertTrue(ex.getMessage().contains("salario"));
    }

    @Test
    void instance_conSupervisorPorDebajoDelPiso_lanzaSalarioInvalidoException() {
        SalarioInvalidoException ex = assertThrows(SalarioInvalidoException.class, () ->
                Empleado.instance("Juan Perez", "ABC1234", LocalDate.of(2024, 3, 15),
                        Puesto.SUPERVISOR, new BigDecimal("799999.99")));

        assertTrue(ex.getMessage().contains("salario"));
    }

    @Test
    void instance_conGerentePorDebajoDelPiso_lanzaSalarioInvalidoException() {
        SalarioInvalidoException ex = assertThrows(SalarioInvalidoException.class, () ->
                Empleado.instance("Juan Perez", "ABC1234", LocalDate.of(2024, 3, 15),
                        Puesto.GERENTE, new BigDecimal("1199999.99")));

        assertTrue(ex.getMessage().contains("salario"));
    }
    @Test
    void instance_conAnalistaEnElPiso_creaEmpleado() {
        Empleado empleado = Empleado.instance("Juan Perez", "ABC1234", LocalDate.of(2024, 3, 15),
                Puesto.ANALISTA, new BigDecimal("500000"));

        assertEquals(new BigDecimal("500000"), empleado.getSalario());
    }

    @Test
    void instance_conSupervisorEnElPiso_creaEmpleado() {
        Empleado empleado = Empleado.instance("Juan Perez", "ABC1234", LocalDate.of(2024, 3, 15),
                Puesto.SUPERVISOR, new BigDecimal("800000"));

        assertEquals(new BigDecimal("800000"), empleado.getSalario());
    }

    @Test
    void instance_conGerenteEnElPiso_creaEmpleado() {
        Empleado empleado = Empleado.instance("Juan Perez", "ABC1234", LocalDate.of(2024, 3, 15),
                Puesto.GERENTE, new BigDecimal("1200000"));

        assertEquals(new BigDecimal("1200000"), empleado.getSalario());
    }
}