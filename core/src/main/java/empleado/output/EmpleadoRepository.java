package empleado.output;

import empleado.modelo.Empleado;

public interface EmpleadoRepository {

    boolean existePorLegajo(String legajo);

    boolean guardar(Empleado empleado);
}