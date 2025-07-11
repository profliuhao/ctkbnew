package org.haoai.medixhub.ctkb.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CTKB REST API")
                        .description("Some custom description of API.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Hao Liu")
                                .url("www.example.com")
                                .email("myeaddress@company.com"))
                        .license(new License()
                                .name("License of API")
                                .url("API license URL")));
    }
}
