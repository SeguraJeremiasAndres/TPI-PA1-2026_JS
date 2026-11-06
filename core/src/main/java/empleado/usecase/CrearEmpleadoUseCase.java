package empleado.usecase;

import empleado.input.CrearEmpleadoCommand;
import empleado.modelo.Empleado;
import empleado.output.EmpleadoRepository;

public class CrearEmpleadoUseCase {

    private final EmpleadoRepository repository;

    public CrearEmpleadoUseCase(EmpleadoRepository repository) {
        this.repository = repository;
    }

    public Empleado ejecutar(CrearEmpleadoCommand command) {
        throw new UnsupportedOperationException("pendiente de implementar");
    }
}
