package com.eliangilsierra.mvcscaffold.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mvcScaffoldOpenApi() {
        return new OpenAPI().info(new Info()
                .title("MVC Architecture Scaffold")
                .description("Reference product catalog API — classic layered (MVC) architecture")
                .version("v1"));
    }
}
