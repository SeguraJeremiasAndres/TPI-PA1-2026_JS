package empleado.usecase;

import empleado.modelo.Empleado;
import empleado.output.EmpleadoRepository;
import empleado.exception.EmpleadoNoEncontradoException;
public class BuscarEmpleadoPorLegajoUseCase {

    private final EmpleadoRepository repository;

    public BuscarEmpleadoPorLegajoUseCase(EmpleadoRepository repository) {
        this.repository = repository;
    }

    public Empleado ejecutar(String legajo) {
        return repository.buscarPorLegajo(legajo)
                .orElseThrow(() -> new EmpleadoNoEncontradoException(
                        "No se encontro un empleado con el legajo " + legajo));
    }
}