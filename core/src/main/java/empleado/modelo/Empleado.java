package empleado.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import empleado.exception.CampoObligatorioException;
public class Empleado {

    private final UUID id;
    private final String nombreCompleto;
    private final String legajo;
    private final LocalDate fechaIngreso;
    private final Puesto puesto;
    private final BigDecimal salario;

    private Empleado(UUID id, String nombreCompleto, String legajo,
                     LocalDate fechaIngreso, Puesto puesto, BigDecimal salario) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.legajo = legajo;
        this.fechaIngreso = fechaIngreso;
        this.puesto = puesto;
        this.salario = salario;
    }

    public static Empleado instance(String nombreCompleto, String legajo,
                                    LocalDate fechaIngreso, Puesto puesto,
                                    BigDecimal salario) {

        validarObligatorio(nombreCompleto, "nombreCompleto");
        validarObligatorio(legajo, "legajo");
        validarObligatorio(fechaIngreso, "fechaIngreso");
        validarObligatorio(puesto, "puesto");
        validarObligatorio(salario, "salario");

        return new Empleado(UUID.randomUUID(), nombreCompleto, legajo,
                fechaIngreso, puesto, salario);
    }
    private static void validarObligatorio(Object valor, String campo) {
        if (valor == null) {
            throw new CampoObligatorioException("El campo " + campo + " es obligatorio");
        }
    }
    public UUID getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getLegajo() { return legajo; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public Puesto getPuesto() { return puesto; }
    public BigDecimal getSalario() { return salario; }
}