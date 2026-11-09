package ar.edu.undec.adapter.data.empleado.repoimplementacion;
import ar.edu.undec.adapter.data.empleado.mapper.EmpleadoEntityMapper;
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
        return crud.existsByLegajo(legajo);
    }

    @Override
    public boolean guardar(Empleado empleado) {
        crud.save(EmpleadoEntityMapper.toEntity(empleado));
        return true;
    }

    @Override
    public Optional<Empleado> buscarPorLegajo(String legajo) {
        return crud.findByLegajo(legajo).map(EmpleadoEntityMapper::toDomain);
    }

    @Override
    public List<Empleado> listarTodos() {
        return crud.findAll().stream()
                .map(EmpleadoEntityMapper::toDomain)
                .toList();
    }
}