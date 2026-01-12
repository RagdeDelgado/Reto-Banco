package com.banco.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@OpenAPIDefinition(
        info = @Info(
                title = "Banco API",
                version = "1.0.0",
                description = "API REST del Banco"
        ),
        servers = {
                @Server(
                        url = "/banco-api",
                        description = "Context root del WAR"
                )
        }
)
public class OpenApiConfig extends Application {

}
