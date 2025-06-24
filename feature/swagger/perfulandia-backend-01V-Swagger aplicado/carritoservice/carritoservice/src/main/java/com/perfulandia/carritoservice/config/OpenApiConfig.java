// config/OpenApiConfig.java
package com.perfulandia.carritoservice.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Perfulandia SPA")
                        .version("1.0.0")
                        .description("Documentación de la API para la tienda de perfumes Perfulandia.")
                        .contact(new Contact()
                                .name("Rodrigo Vargas")
                                .email("ro.vargas@duocuc.cl")
                                .url("https://github.com/Raynagah/PerfulandiaSPA/tree/main/feature/swagger")
                        )

                );
    }
}