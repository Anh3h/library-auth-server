package com.courge.shop.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@org.springframework.context.annotation.Configuration
@EnableSwagger2
public class Configuration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    /*@Bean
    Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.migrate();
        return flyway();
    }*/

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Shop Application",
                "",
                "API V1.0.0",
                "Terms of serivce",
                new Contact("Courage Angeh", "", ""),
                "License of API",
                "API LICENCE URL");
    }

}
