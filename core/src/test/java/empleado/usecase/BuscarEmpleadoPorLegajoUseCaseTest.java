package empleado.usecase;

import empleado.exception.EmpleadoNoEncontradoException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarEmpleadoPorLegajoUseCaseTest {

    @Mock
    private EmpleadoRepository repository;

    private BuscarEmpleadoPorLegajoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new BuscarEmpleadoPorLegajoUseCase(repository);
    }

    @Test
    void ejecutar_conLegajoExistente_retornaElEmpleado() {
        Empleado empleado = Empleado.instance("Juan Perez", "ABC1234",
                LocalDate.of(2024, 3, 15), Puesto.ANALISTA, new BigDecimal("600000"));
        when(repository.buscarPorLegajo("ABC1234")).thenReturn(Optional.of(empleado));

        Empleado resultado = useCase.ejecutar("ABC1234");

        assertSame(empleado, resultado);
    }

    @Test
    void ejecutar_conLegajoInexistente_lanzaEmpleadoNoEncontradoException() {
        when(repository.buscarPorLegajo("ZZZ9999")).thenReturn(Optional.empty());

        EmpleadoNoEncontradoException ex = assertThrows(EmpleadoNoEncontradoException.class,
                () -> useCase.ejecutar("ZZZ9999"));

        assertTrue(ex.getMessage().contains("ZZZ9999"));
    }
}