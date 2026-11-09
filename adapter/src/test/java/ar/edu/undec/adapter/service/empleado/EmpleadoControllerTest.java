package ar.edu.undec.adapter.service.empleado;

import ar.edu.undec.adapter.service.empleado.controller.EmpleadoController;
import empleado.input.CrearEmpleadoCommand;
import empleado.modelo.Empleado;
import empleado.modelo.Puesto;
import empleado.usecase.CrearEmpleadoUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmpleadoController.class)
class EmpleadoControllerTest {

    private static final String JSON_VALIDO = """
            {
              "nombreCompleto": "Juan Perez",
              "legajo": "ABC1234",
              "fechaIngreso": "2024-03-15",
              "puesto": "ANALISTA",
              "salario": 600000
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrearEmpleadoUseCase crearEmpleadoUseCase;

    @Test
    void crear_conDatosValidos_respondeCreatedConElEmpleado() throws Exception {
        Empleado empleado = empleadoValido();
        when(crearEmpleadoUseCase.ejecutar(any(CrearEmpleadoCommand.class)))
                .thenReturn(empleado);

        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(empleado.getId().toString()))
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Perez"))
                .andExpect(jsonPath("$.legajo").value("ABC1234"))
                .andExpect(jsonPath("$.fechaIngreso").value("2024-03-15"))
                .andExpect(jsonPath("$.puesto").value("ANALISTA"))
                .andExpect(jsonPath("$.salario").value(600000));
    }

    @Test
    void crear_deserializaElRequestYLoPasaAlCasoDeUso() throws Exception {
        when(crearEmpleadoUseCase.ejecutar(any(CrearEmpleadoCommand.class)))
                .thenReturn(empleadoValido());

        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_VALIDO))
                .andExpect(status().isCreated());

        ArgumentCaptor<CrearEmpleadoCommand> captor =
                ArgumentCaptor.forClass(CrearEmpleadoCommand.class);
        verify(crearEmpleadoUseCase).ejecutar(captor.capture());
        CrearEmpleadoCommand command = captor.getValue();
        assertEquals("Juan Perez", command.nombreCompleto());
        assertEquals("ABC1234", command.legajo());
        assertEquals(LocalDate.of(2024, 3, 15), command.fechaIngreso());
        assertEquals(Puesto.ANALISTA, command.puesto());
        assertEquals(0, new BigDecimal("600000").compareTo(command.salario()));
    }

    private Empleado empleadoValido() {
        return Empleado.instance("Juan Perez", "ABC1234",
                LocalDate.of(2024, 3, 15), Puesto.ANALISTA, new BigDecimal("600000"));
    }
}
