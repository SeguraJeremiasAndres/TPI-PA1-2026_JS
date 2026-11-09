package empleado.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import empleado.exception.CampoObligatorioException;
import empleado.exception.LegajoInvalidoException;
import java.util.regex.Pattern;
import empleado.exception.FechaIngresoInvalidaException;
import empleado.exception.SalarioInvalidoException;
import empleado.exception.NombreInvalidoException;
public class Empleado {

    private final UUID id;
    private final String nombreCompleto;
    private final String legajo;
    private final LocalDate fechaIngreso;
    private final Puesto puesto;
    private final BigDecimal salario;

    private static final Pattern FORMATO_LEGAJO = Pattern.compile("[A-Z]{3}[0-9]{4}");
    private static final int LONGITUD_MAXIMA_NOMBRE = 100;
    private static final BigDecimal SALARIO_MAXIMO = new BigDecimal("9999999999999.99");
    private static final int DECIMALES_MAXIMOS = 2;

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
        return instance(UUID.randomUUID(), nombreCompleto, legajo,
                fechaIngreso, puesto, salario);
    }
    public static Empleado instance(UUID id, String nombreCompleto, String legajo,
                                    LocalDate fechaIngreso, Puesto puesto,
                                    BigDecimal salario) {
        validarObligatorio(id, "id");
        validarObligatorio(nombreCompleto, "nombreCompleto");
        validarObligatorio(legajo, "legajo");
        validarObligatorio(fechaIngreso, "fechaIngreso");
        validarObligatorio(puesto, "puesto");
        validarObligatorio(salario, "salario");

        validarLongitudNombre(nombreCompleto);
        validarFormatoLegajo(legajo);
        validarFechaIngreso(fechaIngreso);
        validarSalario(salario, puesto);

        return new Empleado(id, nombreCompleto, legajo, fechaIngreso, puesto, salario);
    }

    private static void validarLongitudNombre(String nombreCompleto) {
        if (nombreCompleto.length() > LONGITUD_MAXIMA_NOMBRE) {
            throw new NombreInvalidoException(
                    "El nombreCompleto no puede superar los " + LONGITUD_MAXIMA_NOMBRE + " caracteres");
        }
    }

    private static void validarObligatorio(Object valor, String campo) {
        if (valor == null || (valor instanceof String texto && texto.isBlank())) {
            throw new CampoObligatorioException("El campo " + campo + " es obligatorio");
        }
    }

    private static void validarFormatoLegajo(String legajo) {
        if (!FORMATO_LEGAJO.matcher(legajo).matches()) {
            throw new LegajoInvalidoException(
                    "El legajo debe tener 3 letras mayúsculas seguidas de 4 números (ej. ABC1234)");
        }
    }

    private static void validarFechaIngreso(LocalDate fechaIngreso) {
        if (fechaIngreso.isAfter(LocalDate.now())) {
            throw new FechaIngresoInvalidaException(
                    "La fechaIngreso no puede ser posterior a la fecha actual");
        }
    }

    private static void validarSalario(BigDecimal salario, Puesto puesto) {
        if (salario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SalarioInvalidoException("El salario debe ser mayor a cero");
        }
        if (salario.stripTrailingZeros().scale() > DECIMALES_MAXIMOS) {
            throw new SalarioInvalidoException(
                    "El salario no puede tener mas de " + DECIMALES_MAXIMOS + " decimales");
        }
        if (salario.compareTo(SALARIO_MAXIMO) > 0) {
            throw new SalarioInvalidoException(
                    "El salario no puede superar " + SALARIO_MAXIMO);
        }
        if (salario.compareTo(puesto.getSalarioMinimo()) < 0) {
            throw new SalarioInvalidoException(
                    "El salario para " + puesto + " no puede ser menor a " + puesto.getSalarioMinimo());
        }
    }

    public UUID getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getLegajo() { return legajo; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public Puesto getPuesto() { return puesto; }
    public BigDecimal getSalario() { return salario; }
}