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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
    @Test
    void existePorLegajo_conEmpleadoGuardado_retornaTrue() {
        repository.guardar(empleadoValido());
        entityManager.flush();

        boolean existe = repository.existePorLegajo("ABC1234");

        assertTrue(existe);
    }

    @Test
    void existePorLegajo_conLegajoInexistente_retornaFalse() {
        boolean existe = repository.existePorLegajo("ZZZ9999");

        assertFalse(existe);
    }

    @Test
    void listarTodos_conEmpleadosGuardados_retornaTodos() {
        repository.guardar(empleadoValido());
        repository.guardar(Empleado.instance("Maria Gomez", "XYZ5678",
                LocalDate.of(2023, 8, 1), Puesto.SUPERVISOR, new BigDecimal("900000")));
        entityManager.flush();
        entityManager.clear();

        List<Empleado> resultado = repository.listarTodos();

        Set<String> legajos = resultado.stream()
                .map(Empleado::getLegajo)
                .collect(Collectors.toSet());
        assertEquals(2, resultado.size());
        assertEquals(Set.of("ABC1234", "XYZ5678"), legajos);
    }

    @Test
    void listarTodos_sinEmpleados_retornaListaVacia() {
        List<Empleado> resultado = repository.listarTodos();

        assertTrue(resultado.isEmpty());
    }
}