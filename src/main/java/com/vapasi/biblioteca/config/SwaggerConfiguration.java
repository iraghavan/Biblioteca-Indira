package com.vapasi.biblioteca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket swaggerDocketConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfoDetails())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.vapasi.biblioteca"))
                .build()
                .tags(new Tag("Biblioteca","End points of the biblioteca application"))
                ;
    }

    private ApiInfo apiInfoDetails() {
        return new ApiInfoBuilder().title("The Bangalore Public Library ")
                .description("REST APIs developed by Team Athena@Vapasi 2020")
                .version("2.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Bearer token", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {

        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Bearer token", authorizationScopes));
    }

}
