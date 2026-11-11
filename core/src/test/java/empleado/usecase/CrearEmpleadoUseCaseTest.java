package empleado.usecase;

import empleado.exception.LegajoDuplicadoException;
import empleado.exception.LegajoInvalidoException;
import empleado.exception.PersistenciaException;
import empleado.input.CrearEmpleadoCommand;
import empleado.modelo.Empleado;
import empleado.modelo.Puesto;
import empleado.output.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearEmpleadoUseCaseTest {

    @Mock
    private EmpleadoRepository repository;

    private CrearEmpleadoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CrearEmpleadoUseCase(repository);
    }

    @Test
    void ejecutar_conDatosValidos_guardaYRetornaEmpleado() {
        CrearEmpleadoCommand command = comandoValido();
        when(repository.existePorLegajo("ABC1234")).thenReturn(false);
        when(repository.guardar(any(Empleado.class))).thenReturn(true);

        Empleado resultado = useCase.ejecutar(command);

        assertEquals("Juan Perez", resultado.getNombreCompleto());
        assertEquals("ABC1234", resultado.getLegajo());
        verify(repository).guardar(any(Empleado.class));
    }

    @Test
    void ejecutar_conLegajoExistente_lanzaLegajoDuplicadoExceptionYNoGuarda() {
        CrearEmpleadoCommand command = comandoValido();
        when(repository.existePorLegajo("ABC1234")).thenReturn(true);

        LegajoDuplicadoException ex = assertThrows(LegajoDuplicadoException.class,
                () -> useCase.ejecutar(command));

        assertTrue(ex.getMessage().contains("ABC1234"));
        verify(repository, never()).guardar(any(Empleado.class));
    }

    @Test
    void ejecutar_conDatosInvalidos_lanzaExcepcionDeDominioYNoUsaElRepositorio() {
        CrearEmpleadoCommand command = new CrearEmpleadoCommand("Juan Perez", "abc",
                LocalDate.of(2024, 3, 15), Puesto.ANALISTA, new BigDecimal("600000"));

        assertThrows(LegajoInvalidoException.class, () -> useCase.ejecutar(command));

        verifyNoInteractions(repository);
    }

    @Test
    void ejecutar_cuandoNoSePuedeGuardar_lanzaPersistenciaException() {
        CrearEmpleadoCommand command = comandoValido();
        when(repository.existePorLegajo("ABC1234")).thenReturn(false);
        when(repository.guardar(any(Empleado.class))).thenReturn(false);

        assertThrows(PersistenciaException.class, () -> useCase.ejecutar(command));
    }

    private CrearEmpleadoCommand comandoValido() {
        return new CrearEmpleadoCommand("Juan Perez", "ABC1234",
                LocalDate.of(2024, 3, 15), Puesto.ANALISTA, new BigDecimal("600000"));
    }
}