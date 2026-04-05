package com.raizesdonordeste.backend.configuracao;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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