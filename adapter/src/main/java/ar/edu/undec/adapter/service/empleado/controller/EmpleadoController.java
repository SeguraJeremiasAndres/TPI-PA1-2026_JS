package ar.edu.undec.adapter.service.empleado.controller;

import ar.edu.undec.adapter.service.empleado.mapper.EmpleadoRestMapper;
import ar.edu.undec.adapter.service.empleado.model.CrearEmpleadoRequest;
import ar.edu.undec.adapter.service.empleado.model.EmpleadoResponse;
import empleado.modelo.Empleado;
import empleado.usecase.BuscarEmpleadoPorLegajoUseCase;
import empleado.usecase.CrearEmpleadoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private final CrearEmpleadoUseCase crearEmpleadoUseCase;
    private final BuscarEmpleadoPorLegajoUseCase buscarEmpleadoPorLegajoUseCase;

    public EmpleadoController(CrearEmpleadoUseCase crearEmpleadoUseCase,
                              BuscarEmpleadoPorLegajoUseCase buscarEmpleadoPorLegajoUseCase) {
        this.crearEmpleadoUseCase = crearEmpleadoUseCase;
        this.buscarEmpleadoPorLegajoUseCase = buscarEmpleadoPorLegajoUseCase;
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponse> crear(@RequestBody CrearEmpleadoRequest request) {
        Empleado empleado = crearEmpleadoUseCase.ejecutar(EmpleadoRestMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EmpleadoRestMapper.toResponse(empleado));
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<EmpleadoResponse> buscar(@PathVariable("legajo") String legajo) {
        throw new UnsupportedOperationException("pendiente de implementar");
    }
}