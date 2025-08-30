package br.com.desafio.tecnico.desafio.infraestructure.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("ERP System")
                        .version("v1")
                        .description("Api para um software de ERP criado para desafio tecnico Accenture ")
                        .termsOfService("https://ismael.com.br")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://ismael.com.br")
                        )
                );
    }
}
