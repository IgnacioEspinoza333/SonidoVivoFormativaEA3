package sonidoVivo.tienda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

// Configuración de Swagger: define el título y la descripción de la documentación
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Sonido Vivo")
                        .description("Documentación de los servicios REST de productos, clientes y ventas")
                        .version("1.0"));
    }
}