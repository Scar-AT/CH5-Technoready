package com.techready.meli.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI meliOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MELI Order System API")
                        .description("RESTful API for managing customer orders in the MELI system. "
                                + "Includes CRUD operations, search with filtering and pagination.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("TechReady Team")
                                .email("support@techready.com")
                                .url("https://github.com/Scar-AT/CH5-Technoready"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
