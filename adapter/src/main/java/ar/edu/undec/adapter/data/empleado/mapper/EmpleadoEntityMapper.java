package ar.edu.undec.adapter.data.empleado.mapper;

import ar.edu.undec.adapter.data.empleado.model.EmpleadoEntity;
import empleado.modelo.Empleado;

public class EmpleadoEntityMapper {

    private EmpleadoEntityMapper() {
    }

    public static EmpleadoEntity toEntity(Empleado empleado) {
        return new EmpleadoEntity(
                empleado.getId(),
                empleado.getNombreCompleto(),
                empleado.getLegajo(),
                empleado.getFechaIngreso(),
                empleado.getPuesto(),
                empleado.getSalario());
    }

    public static Empleado toDomain(EmpleadoEntity entity) {
        return Empleado.instance(
                entity.getId(),
                entity.getNombreCompleto(),
                entity.getLegajo(),
                entity.getFechaIngreso(),
                entity.getPuesto(),
                entity.getSalario());
    }
}