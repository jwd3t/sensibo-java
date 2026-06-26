package com.sensibo.platform.pc26010u202318609.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configures the OpenAPI specification exposed by the platform.
 */
@Configuration
public class OpenApiConfiguration {
    // Properties
    @Value("${spring.application.name}")
    String applicationName;

    @Value("${documentation.application.description}")
    String applicationDescription;

    @Value("${documentation.application.version}")
    String applicationVersion;

    // Methods

    /**
     * Builds the OpenAPI document used by Swagger UI and client generation tools.
     *
     * @return configured OpenAPI descriptor
     */
    @Bean
    public OpenAPI learningPlatformOpenApi() {

        // General configuration
        var openApi = new OpenAPI();
        openApi
                .info(new Info()
                        .title(this.applicationName)
                        .description(this.applicationDescription)
                        .version(this.applicationVersion)
                        .contact(new Contact()
                                .name("Sensibo Climate Control")
                                .email("support@sensibo.com")
                                .url("https://sensibo.com/support"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("Sensibo Climate Control Platform wiki Documentation")
                        .url("https://sensibo.wiki.github.io/docs"));

        // Add server configurations
        openApi.servers(List.of(
                new Server()
                        .url("http://localhost:8096")
                        .description("Local Development Environment"),
                new Server()
                        .url("https://staging-api.sensibo.com")
                        .description("Staging Environment"),
                new Server()
                        .url("https://api.sensibo.com")
                        .description("Production Environment")
        ));

        return openApi;
    }
}