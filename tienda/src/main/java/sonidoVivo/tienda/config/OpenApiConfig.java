package sonidoVivo.tienda.config;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("API Tienda Sonido Vivo")
                        .version("1.0")
                        .description("Documentación interactiva de clientes, productos, ventas y autenticación JWT. "
                                + "Para probar: 1) ejecuta POST /api/auth/login (admin / admin1234 o user / user1234), "
                                + "2) copia el token, 3) presiona el botón Authorize y pega solo el token. "
                                + "Rol USER: solo consulta (GET). Rol ADMIN: consulta, crea, edita y elimina."))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    // Agrega a cada endpoint sus respuestas de error posibles y el permiso que necesita
    @Bean
    public OperationCustomizer respuestasYPermisos() {
        return (operation, handlerMethod) -> {
            ApiResponses respuestas = operation.getResponses();
            if (respuestas == null) {
                respuestas = new ApiResponses();
                operation.setResponses(respuestas);
            }

            // El login es público: solo documentamos su error de credenciales
            if (handlerMethod.getBeanType().getSimpleName().equals("AuthController")) {
                respuestas.addApiResponse("401", new ApiResponse().description("Credenciales inválidas"));
                return operation;
            }

            boolean esGet = handlerMethod.hasMethodAnnotation(GetMapping.class);
            boolean esPost = handlerMethod.hasMethodAnnotation(PostMapping.class);
            boolean esPut = handlerMethod.hasMethodAnnotation(PutMapping.class);
            boolean esCompra = handlerMethod.getMethod().getName().equals("comprar");
            boolean tieneIdEnRuta = operation.getParameters() != null
                    && operation.getParameters().stream().anyMatch(p -> "path".equals(p.getIn()));
            boolean esCliente = handlerMethod.getBeanType().getSimpleName().equals("ClienteController");

            respuestas.addApiResponse("401", new ApiResponse().description("No autenticado: falta el token o es inválido"));

            if (!esGet && !esCompra) {
                respuestas.addApiResponse("403", new ApiResponse().description("Acceso denegado: solo el rol ADMIN puede hacerlo"));
            }
            if (esPost || esPut) {
                respuestas.addApiResponse("400", new ApiResponse().description("Datos inválidos (el detalle indica cada campo)"));
            }
            if (tieneIdEnRuta) {
                respuestas.addApiResponse("404", new ApiResponse().description("No existe un registro con ese ID"));
            }
            if (esCliente && (esPost || esPut)) {
                respuestas.addApiResponse("409", new ApiResponse().description("RUT o correo ya registrado"));
            }

            // Indica en la descripción qué rol necesita el endpoint
            String permiso = (esGet || esCompra) ? "Permiso: rol USER o ADMIN." : "Permiso: solo rol ADMIN.";
            String descripcion = operation.getDescription();
            operation.setDescription((descripcion == null ? "" : descripcion + " ") + "**" + permiso + "**");

            return operation;
        };
    }
}