package ar.edu.undec.adapter.service.empleado.beanconfig;

import empleado.output.EmpleadoRepository;
import empleado.usecase.BuscarEmpleadoPorLegajoUseCase;
import empleado.usecase.CrearEmpleadoUseCase;
import empleado.usecase.ListarEmpleadosUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CrearEmpleadoUseCase crearEmpleadoUseCase(EmpleadoRepository repository) {
        return new CrearEmpleadoUseCase(repository);
    }

    @Bean
    public BuscarEmpleadoPorLegajoUseCase buscarEmpleadoPorLegajoUseCase(EmpleadoRepository repository) {
        return new BuscarEmpleadoPorLegajoUseCase(repository);
    }

    @Bean
    public ListarEmpleadosUseCase listarEmpleadosUseCase(EmpleadoRepository repository) {
        return new ListarEmpleadosUseCase(repository);
    }
}