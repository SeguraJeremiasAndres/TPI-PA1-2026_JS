package ar.edu.undec.adapter.data.empleado.repoimplementacion;

import ar.edu.undec.adapter.data.empleado.crud.EmpleadoCrudRepository;
import empleado.modelo.Empleado;
import empleado.output.EmpleadoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmpleadoRepositoryImpl implements EmpleadoRepository {

    private final EmpleadoCrudRepository crud;

    public EmpleadoRepositoryImpl(EmpleadoCrudRepository crud) {
        this.crud = crud;
    }

    @Override
    public boolean existePorLegajo(String legajo) {
        throw new UnsupportedOperationException("pendiente de implementar");
    }

    @Override
    public boolean guardar(Empleado empleado) {
        throw new UnsupportedOperationException("pendiente de implementar");
    }

    @Override
    public Optional<Empleado> buscarPorLegajo(String legajo) {
        throw new UnsupportedOperationException("pendiente de implementar");
    }

    @Override
    public List<Empleado> listarTodos() {
        throw new UnsupportedOperationException("pendiente de implementar");
    }
}