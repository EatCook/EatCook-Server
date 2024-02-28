package com.itcook.cooking.api.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    servers = {
        @Server(
            description = "Local ENV",
            url = "http://localhost:8080"
        )
    },
    tags = {
        @Tag(name = "User", description = "유저 도메인과 관련된 APIs")
    }
)
public class SwaggerConfig {

//    @Bean
//    public ModelResolver modelResolver(ObjectMapper objectMapper) {
//        return new ModelResolver(objectMapper);
//    }

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
            .title("잇쿡 서비스 API")
            .version("0.0")
            .description("잇쿡 서비스에 대한 API입니다.");

        return new OpenAPI()
            .components(new Components())
            .info(info)
            ;
    }
}
