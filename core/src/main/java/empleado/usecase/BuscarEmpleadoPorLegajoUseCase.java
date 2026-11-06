package empleado.usecase;

import empleado.modelo.Empleado;
import empleado.output.EmpleadoRepository;

public class BuscarEmpleadoPorLegajoUseCase {

    private final EmpleadoRepository repository;

    public BuscarEmpleadoPorLegajoUseCase(EmpleadoRepository repository) {
        this.repository = repository;
    }

    public Empleado ejecutar(String legajo) {
        throw new UnsupportedOperationException("pendiente de implementar");
    }
}