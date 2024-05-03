package com.itcook.cooking.api.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        tags = {
                @Tag(name = "01. User", description = "유저 도메인과 관련된 APIs"),
                @Tag(name = "02. CookTalk", description = "쿡톡 도메인과 관련된 APIs"),
                @Tag(name = "03. Recipe", description = "레시피 도메인과 관련된 APIs"),
                @Tag(name = "04. Search", description = "검색과 관련된 APIs"),
                @Tag(name = "05. MyPage", description = "마이페이지와 관련된 APIs"),
                @Tag(name = "06. Archive", description = "보관함 관련된 APIs"),
                @Tag(name = "07. Follow", description = "팔로우 관련된 APIs"),
                @Tag(name = "08. Liked", description = "좋아요 관련된 APIs"),
        }
)
public class SwaggerConfig {

    @Value("${swagger-server.host}")
    private String devHost;

//    @Bean
//    public ModelResolver modelResolver(ObjectMapper objectMapper) {
//        return new ModelResolver(objectMapper);
//    }

    @Bean
    public OpenAPI openAPI() {
        List<Server> servers = getServers();
        License license = new License()
                .name("잇쿡")
                .url("https://github.com/EatCook/EatCook-Server");

        Info info = new Info()
                .title("잇쿡 서비스 API")
                .version("v0.0.1")
                .license(license)
                .description("잇쿡 서비스에 대한 API입니다.");

        return new OpenAPI()
                .components(authSettings())
                .info(info)
                .servers(servers)
                ;
    }

    private Components authSettings() {
        return new Components()
                .addSecuritySchemes("access-token",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                );
    }

    private List<Server> getServers() {
        Server server1 = new Server();
        server1.url("http://localhost:8080");
        server1.description("Local ENV");

        Server server2 = new Server();
        server2.url("http://%s:8080".formatted(devHost));
        server2.description("Dev ENV");

        return List.of(server1, server2);
    }
}
