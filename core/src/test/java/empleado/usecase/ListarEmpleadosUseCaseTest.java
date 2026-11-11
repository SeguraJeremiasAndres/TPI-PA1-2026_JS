package empleado.usecase;

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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarEmpleadosUseCaseTest {

    @Mock
    private EmpleadoRepository repository;

    private ListarEmpleadosUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListarEmpleadosUseCase(repository);
    }

    @Test
    void ejecutar_conEmpleadosRegistrados_retornaTodos() {
        Empleado juan = Empleado.instance("Juan Perez", "ABC1234",
                LocalDate.of(2024, 3, 15), Puesto.ANALISTA, new BigDecimal("600000"));
        Empleado maria = Empleado.instance("Maria Gomez", "XYZ5678",
                LocalDate.of(2023, 8, 1), Puesto.SUPERVISOR, new BigDecimal("900000"));
        when(repository.listarTodos()).thenReturn(List.of(juan, maria));

        List<Empleado> resultado = useCase.ejecutar();

        assertEquals(List.of(juan, maria), resultado);
    }

    @Test
    void ejecutar_sinEmpleadosRegistrados_retornaListaVacia() {
        when(repository.listarTodos()).thenReturn(List.of());

        List<Empleado> resultado = useCase.ejecutar();

        assertTrue(resultado.isEmpty());
    }
}