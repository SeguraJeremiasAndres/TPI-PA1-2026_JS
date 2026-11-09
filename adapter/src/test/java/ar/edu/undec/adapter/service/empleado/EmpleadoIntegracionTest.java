package ar.edu.undec.adapter.service.empleado;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmpleadoIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void crearYBuscar_devuelveElMismoEmpleadoConElMismoId() throws Exception {
        String respuestaCreacion = mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEmpleado("ABC1234", "ANALISTA", "600000")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String id = JsonPath.read(respuestaCreacion, "$.id");

        mockMvc.perform(get("/empleados/ABC1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Perez"))
                .andExpect(jsonPath("$.legajo").value("ABC1234"))
                .andExpect(jsonPath("$.puesto").value("ANALISTA"));
    }

    @Test
    void crear_conLegajoRepetido_respondeConflict() throws Exception {
        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEmpleado("ABC1234", "ANALISTA", "600000")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEmpleado("ABC1234", "GERENTE", "1500000")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(containsString("ABC1234")));
    }

    @Test
    void crear_conSalarioPorDebajoDelPiso_respondeBadRequest() throws Exception {
        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEmpleado("ABC1234", "ANALISTA", "100")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(containsString("salario")));
    }

    @Test
    void buscar_conLegajoInexistente_respondeNotFound() throws Exception {
        mockMvc.perform(get("/empleados/ZZZ9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value(containsString("ZZZ9999")));
    }

    @Test
    void listar_devuelveLosEmpleadosCreados() throws Exception {
        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEmpleado("ABC1234", "ANALISTA", "600000")))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEmpleado("XYZ5678", "SUPERVISOR", "900000")))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    private String jsonEmpleado(String legajo, String puesto, String salario) {
        return """
                {
                  "nombreCompleto": "Juan Perez",
                  "legajo": "%s",
                  "fechaIngreso": "2024-03-15",
                  "puesto": "%s",
                  "salario": %s
                }
                """.formatted(legajo, puesto, salario);
    }
}