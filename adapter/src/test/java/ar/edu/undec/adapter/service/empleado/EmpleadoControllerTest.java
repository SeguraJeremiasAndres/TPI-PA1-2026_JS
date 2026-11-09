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
import empleado.usecase.BuscarEmpleadoPorLegajoUseCase;
import empleado.usecase.ListarEmpleadosUseCase;
import empleado.exception.CampoObligatorioException;
import empleado.exception.EmpleadoNoEncontradoException;
import empleado.exception.FechaIngresoInvalidaException;
import empleado.exception.LegajoDuplicadoException;
import empleado.exception.LegajoInvalidoException;
import empleado.exception.PersistenciaException;
import empleado.exception.SalarioInvalidoException;
import empleado.exception.NombreInvalidoException;
import org.springframework.test.web.servlet.ResultActions;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

    @MockBean
    private BuscarEmpleadoPorLegajoUseCase buscarEmpleadoPorLegajoUseCase;

    @MockBean
    private ListarEmpleadosUseCase listarEmpleadosUseCase;

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

    @Test
    void buscar_conLegajoExistente_respondeOkConElEmpleado() throws Exception {
        Empleado empleado = empleadoValido();
        when(buscarEmpleadoPorLegajoUseCase.ejecutar("ABC1234")).thenReturn(empleado);

        mockMvc.perform(get("/empleados/ABC1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(empleado.getId().toString()))
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Perez"))
                .andExpect(jsonPath("$.legajo").value("ABC1234"))
                .andExpect(jsonPath("$.fechaIngreso").value("2024-03-15"))
                .andExpect(jsonPath("$.puesto").value("ANALISTA"))
                .andExpect(jsonPath("$.salario").value(600000));
    }
    @Test
    void listar_conEmpleadosRegistrados_respondeOkConTodos() throws Exception {
        Empleado juan = empleadoValido();
        Empleado maria = Empleado.instance("Maria Gomez", "XYZ5678",
                LocalDate.of(2023, 8, 1), Puesto.SUPERVISOR, new BigDecimal("900000"));
        when(listarEmpleadosUseCase.ejecutar()).thenReturn(List.of(juan, maria));

        mockMvc.perform(get("/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].legajo").value("ABC1234"))
                .andExpect(jsonPath("$[0].puesto").value("ANALISTA"))
                .andExpect(jsonPath("$[1].legajo").value("XYZ5678"))
                .andExpect(jsonPath("$[1].nombreCompleto").value("Maria Gomez"))
                .andExpect(jsonPath("$[1].puesto").value("SUPERVISOR"))
                .andExpect(jsonPath("$[1].salario").value(900000));
    }

    @Test
    void listar_sinEmpleados_respondeOkConListaVacia() throws Exception {
        when(listarEmpleadosUseCase.ejecutar()).thenReturn(List.of());

        mockMvc.perform(get("/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
    @Test
    void crear_conCampoObligatorioNulo_respondeBadRequestConElMensaje() throws Exception {
        when(crearEmpleadoUseCase.ejecutar(any(CrearEmpleadoCommand.class)))
                .thenThrow(new CampoObligatorioException("El campo legajo es obligatorio"));

        postCrear()
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El campo legajo es obligatorio"));
    }

    @Test
    void crear_conLegajoInvalido_respondeBadRequestConElMensaje() throws Exception {
        when(crearEmpleadoUseCase.ejecutar(any(CrearEmpleadoCommand.class)))
                .thenThrow(new LegajoInvalidoException("El legajo debe tener 3 letras mayusculas y 4 numeros"));

        postCrear()
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El legajo debe tener 3 letras mayusculas y 4 numeros"));
    }

    @Test
    void crear_conFechaIngresoInvalida_respondeBadRequestConElMensaje() throws Exception {
        when(crearEmpleadoUseCase.ejecutar(any(CrearEmpleadoCommand.class)))
                .thenThrow(new FechaIngresoInvalidaException("La fechaIngreso no puede ser posterior a la fecha actual"));

        postCrear()
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La fechaIngreso no puede ser posterior a la fecha actual"));
    }

    @Test
    void crear_conSalarioInvalido_respondeBadRequestConElMensaje() throws Exception {
        when(crearEmpleadoUseCase.ejecutar(any(CrearEmpleadoCommand.class)))
                .thenThrow(new SalarioInvalidoException("El salario debe ser mayor a cero"));

        postCrear()
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El salario debe ser mayor a cero"));
    }

    @Test
    void crear_conLegajoDuplicado_respondeConflictConElMensaje() throws Exception {
        when(crearEmpleadoUseCase.ejecutar(any(CrearEmpleadoCommand.class)))
                .thenThrow(new LegajoDuplicadoException("Ya existe un empleado con el legajo ABC1234"));

        postCrear()
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("Ya existe un empleado con el legajo ABC1234"));
    }

    @Test
    void crear_cuandoFallaLaPersistencia_respondeInternalServerErrorConElMensaje() throws Exception {
        when(crearEmpleadoUseCase.ejecutar(any(CrearEmpleadoCommand.class)))
                .thenThrow(new PersistenciaException("No se pudo guardar el empleado"));

        postCrear()
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.mensaje").value("No se pudo guardar el empleado"));
    }

    @Test
    void buscar_conLegajoInexistente_respondeNotFoundConElMensaje() throws Exception {
        when(buscarEmpleadoPorLegajoUseCase.ejecutar("ZZZ9999"))
                .thenThrow(new EmpleadoNoEncontradoException("No se encontro un empleado con el legajo ZZZ9999"));

        mockMvc.perform(get("/empleados/ZZZ9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No se encontro un empleado con el legajo ZZZ9999"));
    }
    @Test
    void crear_conNombreInvalido_respondeBadRequestConElMensaje() throws Exception {
        when(crearEmpleadoUseCase.ejecutar(any(CrearEmpleadoCommand.class)))
                .thenThrow(new NombreInvalidoException("El nombreCompleto no puede superar los 100 caracteres"));

        postCrear()
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El nombreCompleto no puede superar los 100 caracteres"));
    }
    @Test
    void crear_conPuestoInexistente_respondeBadRequestConMensaje() throws Exception {
        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "nombreCompleto": "Juan Perez",
                              "legajo": "ABC1234",
                              "fechaIngreso": "2024-03-15",
                              "puesto": "DIRECTOR",
                              "salario": 600000
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").isNotEmpty());

        verifyNoInteractions(crearEmpleadoUseCase);
    }

    @Test
    void crear_conJsonMalFormado_respondeBadRequestConMensaje() throws Exception {
        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nombreCompleto\": \"Juan Perez\", "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").isNotEmpty());

        verifyNoInteractions(crearEmpleadoUseCase);
    }

    @Test
    void crear_conFechaConFormatoInvalido_respondeBadRequestConMensaje() throws Exception {
        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "nombreCompleto": "Juan Perez",
                              "legajo": "ABC1234",
                              "fechaIngreso": "15/03/2024",
                              "puesto": "ANALISTA",
                              "salario": 600000
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").isNotEmpty());

        verifyNoInteractions(crearEmpleadoUseCase);
    }

    private ResultActions postCrear() throws Exception {
        return mockMvc.perform(post("/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_VALIDO));
    }
    private Empleado empleadoValido() {
        return Empleado.instance("Juan Perez", "ABC1234",
                LocalDate.of(2024, 3, 15), Puesto.ANALISTA, new BigDecimal("600000"));
    }
}
