package empleado.usecase;

import empleado.input.CrearEmpleadoCommand;
import empleado.modelo.Empleado;
import empleado.output.EmpleadoRepository;
import empleado.exception.LegajoDuplicadoException;
import empleado.exception.PersistenciaException;

public class CrearEmpleadoUseCase {

    private final EmpleadoRepository repository;

    public CrearEmpleadoUseCase(EmpleadoRepository repository) {
        this.repository = repository;
    }

    public Empleado ejecutar(CrearEmpleadoCommand command) {
        Empleado empleado = Empleado.instance(
                command.nombreCompleto(),
                command.legajo(),
                command.fechaIngreso(),
                command.puesto(),
                command.salario());

        if (repository.existePorLegajo(empleado.getLegajo())) {
            throw new LegajoDuplicadoException(
                    "Ya existe un empleado con el legajo " + empleado.getLegajo());
        }

        boolean guardado = repository.guardar(empleado);

        if (!guardado) {
            throw new PersistenciaException("No se pudo guardar el empleado");
        }

        return empleado;
    }
}
