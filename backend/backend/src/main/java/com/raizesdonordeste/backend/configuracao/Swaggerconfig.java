package com.raizesdonordeste.backend.configuracao;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = "bearerAuth",
        description = "Cole o token JWT gerado no login aqui",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class Swaggerconfig {

    @Bean
    public OpenAPI raizesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Raízes do Nordeste API")
                        .description("API para gestão de pedidos multicanal (App, Totem, Balcão)")
                        .version("v1.0.0"));
    }
}