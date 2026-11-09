package ar.edu.undec.adapter.data.empleado.crud;

import ar.edu.undec.adapter.data.empleado.model.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmpleadoCrudRepository extends JpaRepository<EmpleadoEntity, UUID> {
}