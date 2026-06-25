package br.com.rivaldo.helpdeskbff.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${springdoc.openapi.title}") final String title,
            @Value("${springdoc.openapi.description}") final String description,
            @Value("${springdoc.openapi.version}") final String version
    ) {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                )
                // Vincula a segurança globalmente para todos os controllers da tela do BFF
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP) // Define que é uma segurança HTTP
                                        .scheme("bearer")               // Força o Swagger a colocar a palavra "Bearer " automaticamente
                                        .bearerFormat("JWT")            // Especifica o formato do Token
                                        .description("Insira APENAS o código puro do token gerado no login (sem a palavra Bearer na frente)")
                        )
                );
    }
}