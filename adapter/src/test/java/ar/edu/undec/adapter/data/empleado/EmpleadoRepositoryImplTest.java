package ar.edu.undec.adapter.data.empleado;

import ar.edu.undec.adapter.data.empleado.repoimplementacion.EmpleadoRepositoryImpl;
import empleado.modelo.Empleado;
import empleado.modelo.Puesto;
import empleado.output.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(EmpleadoRepositoryImpl.class)
class EmpleadoRepositoryImplTest {

    @Autowired
    private EmpleadoRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void guardar_conEmpleadoValido_retornaTrue() {
        boolean guardado = repository.guardar(empleadoValido());

        assertTrue(guardado);
    }

    @Test
    void buscarPorLegajo_conEmpleadoGuardado_retornaEmpleadoConTodosSusDatos() {
        Empleado empleado = empleadoValido();
        repository.guardar(empleado);
        entityManager.flush();
        entityManager.clear();

        Optional<Empleado> resultado = repository.buscarPorLegajo("ABC1234");

        assertTrue(resultado.isPresent());
        Empleado recuperado = resultado.get();
        assertEquals(empleado.getId(), recuperado.getId());
        assertEquals("Juan Perez", recuperado.getNombreCompleto());
        assertEquals("ABC1234", recuperado.getLegajo());
        assertEquals(LocalDate.of(2024, 3, 15), recuperado.getFechaIngreso());
        assertEquals(Puesto.ANALISTA, recuperado.getPuesto());
        assertEquals(0, new BigDecimal("600000").compareTo(recuperado.getSalario()));
    }

    @Test
    void buscarPorLegajo_conLegajoInexistente_retornaVacio() {
        Optional<Empleado> resultado = repository.buscarPorLegajo("ZZZ9999");

        assertTrue(resultado.isEmpty());
    }

    private Empleado empleadoValido() {
        return Empleado.instance("Juan Perez", "ABC1234",
                LocalDate.of(2024, 3, 15), Puesto.ANALISTA, new BigDecimal("600000"));
    }
}