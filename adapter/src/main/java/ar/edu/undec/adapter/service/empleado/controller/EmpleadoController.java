package ar.edu.undec.adapter.service.empleado.controller;

import ar.edu.undec.adapter.service.empleado.model.CrearEmpleadoRequest;
import ar.edu.undec.adapter.service.empleado.model.EmpleadoResponse;
import empleado.usecase.CrearEmpleadoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.undec.adapter.service.empleado.mapper.EmpleadoRestMapper;
import empleado.modelo.Empleado;
import org.springframework.http.HttpStatus;
@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private final CrearEmpleadoUseCase crearEmpleadoUseCase;

    public EmpleadoController(CrearEmpleadoUseCase crearEmpleadoUseCase) {
        this.crearEmpleadoUseCase = crearEmpleadoUseCase;
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponse> crear(@RequestBody CrearEmpleadoRequest request) {
        Empleado empleado = crearEmpleadoUseCase.ejecutar(EmpleadoRestMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EmpleadoRestMapper.toResponse(empleado));
    }
}