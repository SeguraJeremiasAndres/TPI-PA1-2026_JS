package empleado.usecase;

import empleado.modelo.Empleado;
import empleado.output.EmpleadoRepository;

import java.util.List;

public class ListarEmpleadosUseCase {

    private final EmpleadoRepository repository;

    public ListarEmpleadosUseCase(EmpleadoRepository repository) {
        this.repository = repository;
    }

    public List<Empleado> ejecutar() {
        return repository.listarTodos();
    }
}